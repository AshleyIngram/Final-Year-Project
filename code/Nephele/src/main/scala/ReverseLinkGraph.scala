/**
 * Find the Reverse Link Graph using Nephele/Stratosphere
 */
import eu.stratosphere.client.{RemoteExecutor, LocalExecutor}

import eu.stratosphere.api.scala._
import eu.stratosphere.api.scala.operators._

import AshleyIngram.FYP.Core

object ReverseLinkGraph {
  def main(args: Array[String]) {
    val in = args(0)
    val out = args(1)
   // val jobManagerIp = args(2)
   // val jobManagerPort = args(3).toInt
   //  val jarFile = args(4)

    // val input = DataSource(in, BinarySerializedInputFormat[(String, String)]())
    val input = DataSource(in, CsvInputFormat[(String, String)]())
    val links = input.flatMap(i => Core.ReverseLinkGraph.map(i._1, i._2))
    val result = links.groupBy { case(link, _) => link }.reduce { (l1, l2) => (l1._1, l1._2 + ", " + l2._2) }

    val output = result.write(out, CsvOutputFormat())
    val plan = new ScalaPlan(Seq(output))

    // val executor = new RemoteExecutor(jobManagerIp, jobManagerPort, jarFile)
    // executor.execute(plan)
    System.out.println(LocalExecutor.execute(plan))
  }
}
