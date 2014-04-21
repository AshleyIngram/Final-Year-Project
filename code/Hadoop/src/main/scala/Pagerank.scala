/**
 * Copyright 2011,2012 National ICT Australia Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package AshleyIngram.FYP.Hadoop

import com.nicta.scoobi.Scoobi._
import scala.annotation.tailrec
import org.apache.hadoop.io.Text
import AshleyIngram.FYP.Core

object PageRank extends ScoobiApp with PageRank {

  def run() {
    val input = args(0)
    val output = args(1)

    val data: DList[(Text, Text)] = fromSequenceFile[Text, Text](args(0))

    // Get ReverseLinkGraph for use in PageRank
    val result: DList[(String, Iterable[String])] = data.mapFlatten[(String, String)](in => Core.ReverseLinkGraph.map(in._1.toString(), in._2.toString))
      .groupByKey

    // Take the article title as the key (twice because key and value are the same, rather
    // than having a numerical ID key and a string article name value)
    val urls = result.keys.map(k => (k, k))

    // Graph is the reverse link graph, with each page boxed into a Vertex
    val graph = result.map(r => new Vertex(r._1, r._2.toSeq))

    persist(getPageRanks(urls, graph).toDelimitedTextFile(output + "result"))
  }
}

/**
 * This trait computes the page rank of a graph of pages
 */
trait PageRank {
  val Node = """^(\d+): (.*)$""".r

  /** a Vertex is described as a page id and a list of incoming links */
  type Vertex[K] = (K, Seq[K])
  /** a Graph is described as a list of vertices */
  type Graph[K] = DList[Vertex[K]]
  /** a Score is: the current page rank, the previous page rank and the list of incoming links */
  type Score[K] =  (Float, Float, Seq[K])
  /** a Ranking is the association of: a vertex and a score */
  type Ranking[K] = (K, Score[K])
  /** list of Rankings */
  type Rankings[K] = DList[Ranking[K]]

  /** initialise the vertices of the graph with default scores */
  def initialise[K : WireFormat](inputs: Graph[K]): Rankings[K] = {
    inputs.map { case (url, links) => (url, (1f, 0f, links)) }
  }

  /** @return the page rank for each url */
  def getPageRanks(urls: DList[(String, String)], graph: Graph[String])(implicit configuration: ScoobiConfiguration) = {
    val (_, rankings) = calculateRankings(10.0f, initialise[String](graph))
    val pageRanks = rankings.map { case (id, (pr,_,_)) => (id, pr) }
    (urls join pageRanks).values
  }

  /** @return new rankings */
  def updateRankings[K](previous: DList[Ranking[K]], d: Float = 0.5f)(implicit configuration: ScoobiConfiguration, wf: WireFormat[K], grouping: Grouping[K]) = {
    val outbound: DList[(K, Float)] = previous.mapFlatten { case t @ (url, (pageRank, _, links)) =>
      links.map { link => (link, pageRank / links.size) }
    }
    (previous coGroup outbound) map { case (url, (prevData, outboundMass)) =>
      val newPageRank = (1 - d) + d * outboundMass.sum
      (url, prevData.headOption.map { case (oldPageRank, _, links) =>
        (newPageRank, oldPageRank, links) }.getOrElse((newPageRank, 0f, Nil)))
    }
  }

  /**
   * @param delta maximum observed value between a new score and an old score
   * @param previous previous set of rankings
   * @return a new delta and new set of rankings
   */
  @tailrec
  private def calculateRankings(delta: Float, previous: Rankings[String])
                               (implicit configuration: ScoobiConfiguration): (Float, Rankings[String]) = {
    if (delta <= 1.0f) (delta, previous)
    else {
      val next = updateRankings(previous)
      val maxDelta = next.map { case (_, (n, o, _)) => math.abs(n - o) }.max
      calculateRankings(maxDelta.run, next)
    }
  }
}