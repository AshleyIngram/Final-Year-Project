/**
* Hadoop MapReduce job designed to count the frequency of words
* in an input file
*/
import com.twitter.scalding._

class WordCount(args: Args) extends Job(args)
{
   val source = WritableSequenceFile(args("input"), ('key, 'value))
  
   source.read
         .flatMap('value -> 'word) { line:String => line.split("""\s+""") }
         .groupBy('word) { _.size }
         .write(Tsv(args("output")))
}
