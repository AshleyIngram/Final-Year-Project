package AshleyIngram.FYP.Nephele.ReverseLinkGraph;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.TextInputFormat;

import eu.stratosphere.api.common.Plan;
import eu.stratosphere.api.common.Program;
import eu.stratosphere.api.common.ProgramDescription;
import eu.stratosphere.api.common.operators.FileDataSink;
import eu.stratosphere.api.java.record.functions.FunctionAnnotation.ConstantFields;
import eu.stratosphere.api.java.record.functions.MapFunction;
import eu.stratosphere.api.java.record.functions.ReduceFunction;
import eu.stratosphere.api.java.record.io.CsvOutputFormat;
import eu.stratosphere.api.java.record.operators.MapOperator;
import eu.stratosphere.api.java.record.operators.ReduceOperator;
import eu.stratosphere.api.java.record.operators.ReduceOperator.Combinable;
import eu.stratosphere.client.LocalExecutor;
import eu.stratosphere.hadoopcompatibility.HadoopDataSource;
import eu.stratosphere.types.IntValue;
import eu.stratosphere.types.Record;
import eu.stratosphere.types.StringValue;
import eu.stratosphere.util.Collector;

import scala.Tuple2;

/**
 * Implements a Reverse Link Graph
 */
public class ReverseLinkGraph implements Program, ProgramDescription {

    private static final long serialVersionUID = 1L;


    /**
     * Gets a set of all link pairs
     */
    public static class GetLinkPairs extends MapFunction implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public void map(Record record, Collector<Record> collector) {
            // Get input data from Stratosphere record
            String key = record.getField(1, StringValue.class).getValue();
            String value = record.getField(2, StringValue.class).getValue();

            // Perform actual map operation
            scala.collection.Iterable<Tuple2<String, String>> scalaPairs;
            scalaPairs = AshleyIngram.FYP.Core.ReverseLinkGraph.map(key, value);

            // Convert to Java collection for iteration
            Tuple2<String, String>[] pairs = new Tuple2[scalaPairs.size()];
            scalaPairs.copyToArray(pairs);

            // Output from map
            for(Tuple2<String, String> pair : pairs) {
                collector.collect(new Record(new StringValue(pair._1()), new StringValue(pair._2())));
            }
        }
    }

    /**
     * Groups the results by key, grouping them by the destination
     */
    @Combinable
    @ConstantFields(0)
    public static class GroupByKey extends ReduceFunction implements Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public void reduce(Iterator<Record> records, Collector<Record> out) throws Exception {
            Record element = null;
            StringValue result = "";

            while (records.hasNext()) {
                element = records.next();
                result.append(element.getFieldInto(1, StringValue.class).getValue();
            }

            out.collect(element);
        }

        @Override
        public void combine(Iterator<Record> records, Collector<Record> out) throws Exception {
            // the logic is the same as in the reduce function, so simply call the reduce method
            reduce(records, out);
        }
    }


    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    @Override
    public Plan getPlan(String... args) {
        // parse job parameters
        int numSubTasks   = (args.length > 0 ? Integer.parseInt(args[0]) : 1);
        String dataInput = (args.length > 1 ? args[1] : "");
        String output    = (args.length > 2 ? args[2] : "");


        HadoopDataSource source = new HadoopDataSource(new SequenceFileInputFormat<Text, Text>(), new JobConf(), "Input Lines");
        TextInputFormat.addInputPath(source.getJobConf(), new Path(dataInput));

        MapOperator mapper = MapOperator.builder(new TokenizeLine())
                .input(source)
                .name("Get Link Pairs")
                .build();
        ReduceOperator reducer = ReduceOperator.builder(CountWords.class, StringValue.class, 0)
                .input(mapper)
                .name("Group by Key")
                .build();
        FileDataSink out = new FileDataSink(new CsvOutputFormat(), output, reducer, "Word Counts");
        CsvOutputFormat.configureRecordFormat(out)
                .recordDelimiter('\n')
                .fieldDelimiter(' ')
                .field(StringValue.class, 0)
                .field(IntValue.class, 1);

        Plan plan = new Plan(out, "Reverse Link Graph");
        plan.setDefaultParallelism(numSubTasks);
        return plan;
    }


    @Override
    public String getDescription() {
        return "Parameters: [numSubStasks] [input] [output]";
    }


    public static void main(String[] args) throws Exception {
        ReverseLinkGraph rlg = new ReverseLinkGraph();

        if (args.length < 3) {
            System.err.println(rlg.getDescription());
            System.exit(1);
        }

        Plan plan = rlg.getPlan(args);

        // This will execute the word-count embedded in a local context. replace this line by the commented
        // succeeding line to send the job to a local installation or to a cluster for execution
        LocalExecutor.execute(plan);
//		PlanExecutor ex = new RemoteExecutor("localhost", 6123, "target/pact-examples-0.4-SNAPSHOT-WordCount.jar");
//		ex.executePlan(plan);
    }
}