/**
 * Find Reverse Link Graph using Hadoop
 */
package AshleyIngram.FYP.Hadoop

import com.nicta.scoobi.Scoobi._
import org.apache.hadoop.io.Text
import AshleyIngram.FYP.Core

object ReverseLinkGraph extends ScoobiApp {
  def run() {
    val data: DList[(Text, Text)] = fromSequenceFile[Text, Text](args(0))
    val result = data.mapFlatten[(String, String)](in => Core.ReverseLinkGraph.map(in._1.toString(), in._2.toString))
        .groupByKey

    persist(result.toTextFile(args(1)))
  }
}
