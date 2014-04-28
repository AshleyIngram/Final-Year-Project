package eu.stratosphere.example.java.record.wordcount;

import java.util.Iterator;
import java.util.StringTokenizer;

import eu.stratosphere.api.common.JobExecutionResult;
import eu.stratosphere.api.common.Plan;
import eu.stratosphere.api.common.Program;
import eu.stratosphere.api.common.ProgramDescription;
import eu.stratosphere.api.common.operators.FileDataSink;
import eu.stratosphere.api.common.operators.FileDataSource;
import eu.stratosphere.api.java.record.functions.FunctionAnnotation.ConstantFields;
import eu.stratosphere.api.java.record.functions.MapFunction;
import eu.stratosphere.api.java.record.functions.ReduceFunction;
import eu.stratosphere.api.java.record.io.CsvOutputFormat;
import eu.stratosphere.api.java.record.io.TextInputFormat;
import eu.stratosphere.api.java.record.operators.MapOperator;
import eu.stratosphere.api.java.record.operators.ReduceOperator;
import eu.stratosphere.api.java.record.operators.ReduceOperator.Combinable;
import eu.stratosphere.client.LocalExecutor;
import eu.stratosphere.types.IntValue;
import eu.stratosphere.types.Record;
import eu.stratosphere.types.StringValue;
import eu.stratosphere.util.Collector;

import scala.Tuple2;

/**
 * Implements a word count which takes the input file and counts the number of
 * the occurrences of each word in the file.
 */
public class WordCount implements Program, ProgramDescription {

	private static final long serialVersionUID = 1L;


	/**
	 * Converts a Record containing one string in to multiple string/integer pairs.
	 * The string is tokenized by whitespaces. For each token a new record is emitted,
	 * where the token is the first field and an Integer(1) is the second field.
	 */
	public static class TokenizeLine extends MapFunction {
		private static final long serialVersionUID = 1L;

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

			// perform actual maps
			scala.collection.Iterable<Tuple2<String, String>> scalaPairs;
			scalaPairs = AshleyIngram.FYP.Core.ReverseLinkGraph.map(key, value);

			// Convert to Java collection to iteration
			Tuple2<String, String>[] pairs = new Tuple2[scalaPairs.size()];
			scalaPairs.copyToArray(pairs);

			// output from map
			for(Tuple2<String, String> pair : pairs) {
				collector.collect(new Record(new StringValue(pair._1()), new StringValue(pair._2())));
			}
		}
	}

	/**
	 * Sums up the counts for a certain given key. The counts are assumed to be at position <code>1</code>
	 * in the record. The other fields are not modified.
	 */
	@Combinable
	@ConstantFields(0)
	public static class CountWords extends ReduceFunction {

		private static final long serialVersionUID = 1L;

		@Override
		public void reduce(Iterator<Record> records, Collector<Record> out) throws Exception {
			Record element = null;
			String result = "";
			// String key = element.getField(0, StringValue.class).getValue();
			// result += key += "|";
			result += "|";

			while (records.hasNext()) {
				element = records.next();
				// Add the link source and a comma (source1, source2, etc)
				result += element.getField(1, StringValue.class).getValue();
				result += ", ";
			}

			result += "\n";

			// Change the second value in the record to be the combined string
			element.setField(1, new StringValue(result));

			// output to the world!
			out.collect(element);
		}

		@Override
		public void combine(Iterator<Record> records, Collector<Record> out) throws Exception {
			// the logic is the same as in the reduce function, so simply call the reduce method
			reduce(records, out);
		}
	}


	@Override
	public Plan getPlan(String... args) {
		// parse job parameters
		int numSubTasks   = (args.length > 0 ? Integer.parseInt(args[0]) : 1);
		String dataInput = (args.length > 1 ? args[1] : "");
		String output    = (args.length > 2 ? args[2] : "");

		FileDataSource source = new FileDataSource(new TextInputFormat(), dataInput, "Input Lines");
		MapOperator mapper = MapOperator.builder(new TokenizeLine())
			.input(source)
			.name("Get Links")
			.build();
		ReduceOperator reducer = ReduceOperator.builder(CountWords.class, StringValue.class, 0)
			.input(mapper)
			.name("Group")
			.build();
		@SuppressWarnings("unchecked")
		FileDataSink out = new FileDataSink(new CsvOutputFormat("\n", ",", StringValue.class, StringValue.class), output, reducer, "Word Counts");

		Plan plan = new Plan(out, "Reverse Link Graph");
		plan.setDefaultParallelism(numSubTasks);
		return plan;
	}


	@Override
	public String getDescription() {
		return "Parameters: <numSubStasks> <input> <output>";
	}


	public static void main(String[] args) throws Exception {
		WordCount wc = new WordCount();

		if (args.length < 3) {
			System.err.println(wc.getDescription());
			System.exit(1);
		}

		Plan plan = wc.getPlan(args);

		// This will execute the word-count embedded in a local context. replace this line by the commented
		// succeeding line to send the job to a local installation or to a cluster for execution
		JobExecutionResult result = LocalExecutor.execute(plan);
		System.err.println("Total runtime: " + result.getNetRuntime());
//		PlanExecutor ex = new RemoteExecutor("localhost", 6123, "stratosphere-java-examples-0.4-WordCount.jar");
//		ex.executePlan(plan);
	}
}
