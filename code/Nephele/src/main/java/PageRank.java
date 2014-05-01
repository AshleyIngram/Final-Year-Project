package AshleyIngram.FYP.Nephele;

import java.io.Serializable;
import java.util.Iterator;

import eu.stratosphere.api.common.Plan;
import eu.stratosphere.api.common.Program;
import eu.stratosphere.api.common.ProgramDescription;
import eu.stratosphere.api.common.operators.BulkIteration;
import eu.stratosphere.api.common.operators.FileDataSink;
import eu.stratosphere.api.common.operators.FileDataSource;
import eu.stratosphere.api.java.record.functions.JoinFunction;
import eu.stratosphere.api.java.record.functions.ReduceFunction;
import eu.stratosphere.api.java.record.functions.FunctionAnnotation.ConstantFields;
import eu.stratosphere.api.java.record.functions.MapFunction;
import eu.stratosphere.api.java.record.io.TextInputFormat;
import eu.stratosphere.api.java.record.operators.JoinOperator;
import eu.stratosphere.api.java.record.operators.MapOperator;
import eu.stratosphere.api.java.record.operators.ReduceOperator;
import eu.stratosphere.api.java.record.operators.ReduceOperator.Combinable;
import eu.stratosphere.types.DoubleValue;
import eu.stratosphere.types.LongValue;
import eu.stratosphere.types.Record;
import eu.stratosphere.types.StringValue;
import eu.stratosphere.types.IntValue;
import eu.stratosphere.util.Collector;
import scala.Tuple2;


public class PageRank implements Program, ProgramDescription {
    // --------------------------------------------------------------------------------------------

    public static class GetLinkPairs extends MapFunction {
        @Override
        public void map(Record record, Collector<Record> collector) {
            // get the first field (as type StringValue) from the record
            String line = record.getField(0, StringValue.class).getValue();

            // split the csv thing
            String[] parts = line.split(",");
            String key = parts[0];
            String value = "";

            for(int i = 1; i < parts.length; i++) {
                value += parts[i] += ",";
            }

	    value.replace("\"\"", "\"");

            // perform actual maps
            scala.collection.Iterable<String> scalaLinks = AshleyIngram.FYP.Core.ReverseLinkGraph.findLinkDestinations(value);

            // Convert to Java collection to iteration
            String[] links = new String[scalaLinks.size()];
            scalaLinks.copyToArray(links);

            // output from map
	    AshleyIngram.FYP.Nephele.StringArrayView linksRecord = new AshleyIngram.FYP.Nephele.StringArrayView();
            for (int i = 0; i < links.length; i++) {
                linksRecord.add(links[i]);
            }

	    Record result = new Record();
	    result.setField(0, new StringValue(key));
	    result.setField(1, new DoubleValue(0.5));
	    result.setField(2, linksRecord);
	    collector.collect(result);
        }
    }

    @Combinable
    public static class CombineLinks extends ReduceFunction {
        @Override
        public void reduce(Iterator<Record> records, Collector<Record> out) {
            Record element = new Record();

            // Get the first record to get the key
            if (records.hasNext()) {
                Record r = records.next();
                element.setField(0, r.getField(0, StringValue.class));

                // Put edges in an array
                AshleyIngram.FYP.Nephele.StringArrayView sources = new AshleyIngram.FYP.Nephele.StringArrayView();
                do
                {
                    Record current = records.next();
                    sources.add(current.getField(1, StringValue.class).getValue());
                }
                while(records.hasNext());

                // Default rank
                element.setField(1, new DoubleValue(0.5));

                // Set edges
                element.setField(2, sources);

                out.collect(element);
            }
        }
    }

    public static final class JoinVerexWithEdgesMatch extends JoinFunction implements Serializable {
        private static final long serialVersionUID = 1L;

        private Record record = new Record();
        private StringValue vertexID = new StringValue();
        private DoubleValue partialRank = new DoubleValue();
        private DoubleValue rank = new DoubleValue();

        private AshleyIngram.FYP.Nephele.StringArrayView adjacentNeighbors = new AshleyIngram.FYP.Nephele.StringArrayView();

        @Override
        public void join(Record pageWithRank, Record nullRecord, Collector<Record> out) throws Exception {
            rank = pageWithRank.getField(1, rank);
            adjacentNeighbors = pageWithRank.getField(2, adjacentNeighbors);
            int numNeighbors = adjacentNeighbors.size();

            double rankToDistribute = rank.getValue() / (double) numNeighbors;

            partialRank.setValue(rankToDistribute);
            record.setField(1, partialRank);

            for (int n = 0; n < numNeighbors; n++) {
                vertexID.setValue(adjacentNeighbors.getQuick(n));
                record.setField(0, vertexID);
                out.collect(record);
            }
        }
    }

    @Combinable
    @ConstantFields(0)
    public static final class AggregatingReduce extends ReduceFunction implements Serializable {
        private static final long serialVersionUID = 1L;

        private final DoubleValue sum = new DoubleValue();

        @Override
        public void reduce(Iterator<Record> pageWithPartialRank, Collector<Record> out) throws Exception {
            Record rec = null;
            double rankSum = 0.0;

            while (pageWithPartialRank.hasNext()) {
                rec = pageWithPartialRank.next();
                rankSum += rec.getField(1, DoubleValue.class).getValue();
            }
            sum.setValue(rankSum);

            rec.setField(1, sum);
            out.collect(rec);
        }
    }

    public static final class JoinOldAndNew extends JoinFunction implements Serializable {
        private static final long serialVersionUID = 1L;

        private Record record = new Record();
        private StringValue vertexID = new StringValue();
        private DoubleValue newRank = new DoubleValue();
        private DoubleValue rank = new DoubleValue();

        @Override
        public void join(Record pageWithRank, Record newPageWithRank, Collector<Record> out) throws Exception {
            rank = pageWithRank.getField(1, rank);
            newRank = newPageWithRank.getField(1, newRank);
            vertexID = pageWithRank.getField(0, vertexID);

            double epsilon = 0.00;
            double criterion = rank.getValue() - newRank.getValue();

             if(Math.abs(criterion) > epsilon)
             {
                record.setField(0, new IntValue(1));
             }
        }
    }


    // --------------------------------------------------------------------------------------------

    public Plan getPlan(String ... args) {
        int dop = 1;
        String dataInput = "";
        String outputPath = "";
        int numIterations = 3;
        long numVertices = 5;

        if (args.length >= 3) {
            dop = Integer.parseInt(args[0]);
            dataInput = args[1];
            outputPath = args[2];
        }

        FileDataSource source = new FileDataSource(new TextInputFormat(), dataInput, "Input Lines");

        // Get data in a graph format
        MapOperator mapper = MapOperator.builder(new GetLinkPairs())
                .input(source)
                .name("Get Links")
                .build();

        // Perform actual calculation
        BulkIteration iteration = new BulkIteration("Page Rank Loop");
        iteration.setInput(mapper);

        JoinOperator join = JoinOperator.builder(new JoinVerexWithEdgesMatch(), StringValue.class, 0, 0)
                .input1(iteration.getPartialSolution())
                .input2(iteration.getPartialSolution())
                .name("Join with Edges")
                .build();

        ReduceOperator rankAggregation = ReduceOperator.builder(new AggregatingReduce(), StringValue.class, 0)
                .input(join)
                .name("Rank Aggregation")
                .build();

        iteration.setNextPartialSolution(rankAggregation);
        iteration.setMaximumNumberOfIterations(numIterations);

        FileDataSink out = new FileDataSink(new PageWithRankOutFormat(), outputPath, iteration, "Final Ranks");

        Plan p = new Plan(out, "Simple PageRank");
        p.setDefaultParallelism(dop);
        return p;
    }

    @Override
    public String getDescription() {
        return "Parameters: <degree-of-parallelism> <input> <output>";
    }
}
