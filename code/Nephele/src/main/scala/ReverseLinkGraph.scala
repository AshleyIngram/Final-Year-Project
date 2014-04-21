/**
 * Find the Reverse Link Graph using Nephele/Stratosphere
 */
/*
import eu.stratosphere.client.{RemoteExecutor, LocalExecutor}

import eu.stratosphere.api.scala._
import eu.stratosphere.api.scala.operators._
import eu.stratosphere.hadoopcompatibility.{HadoopDataSource}
import org.apache.hadoop.io.Text
import org.apache.hadoop.fs.Path

import AshleyIngram.FYP.Core
import org.apache.hadoop.mapred.{FileInputFormat, JobConf, SequenceFileInputFormat}

object ReverseLinkGraph {
  def main(args: Array[String]) {
    val in = args(0)
    val out = args(1)
    val jobManagerIp = args(2)
    val jobManagerPort = args(3).toInt
    val jarFile = args(4)

    ////val input = DataSource(in, BinarySerializedInputFormat[(String, String)]())
    ////val input = TextFile(in)
    val jobConf = new JobConf()
    val inputFormat = new SequenceFileInputFormat[Text, Text]()
    val input = new HadoopDataSource[Text, Text](inputFormat, jobConf, "Reverse Link Graph")

    FileInputFormat.addInputPath(jobConf, new Path(args(0)))

    val links = input.flatMap(i => {
       val parts = i.split(',')
       val title = parts(0)
       val body = parts.tail.mkString(",").replace("\"\"", "\"")
       Core.ReverseLinkGraph.map(title, body)
    })

    val result = links.groupBy { case(link, _) => link }.reduce { (l1, l2) => (l1._1, l1._2 + ", " + l2._2) }

    val output = result.write(out, CsvOutputFormat())
    val plan = new ScalaPlan(Seq(output))

    val executor = new RemoteExecutor(jobManagerIp, jobManagerPort, jarFile)
    executor.executePlan(plan)
    ////System.out.println(LocalExecutor.execute(plan))
  }
}
*/