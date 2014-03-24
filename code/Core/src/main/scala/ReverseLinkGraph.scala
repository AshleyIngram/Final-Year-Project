/**
 * Core code designed to calculate a Reverse Link Graph using
 * the MapReduce paradigm
 */
package AshleyIngram.FYP.Core

import org.jsoup.Jsoup
import scala.collection.JavaConversions._

object ReverseLinkGraph {
  /**
   * Map step of calculating a Reverse Link Graph
   * Take a web page (as a string) and return a list of link
   * destinations
   *
   * @param key The name of the article being processed
   * @param value The text of the article being processed
   * @return A tuple for each link (Link Destination, Link Source)
   */
  def map(key: String, value: String): Iterable[(String, String)] = {
    val addresses = findLinkDestinations(value)
    addresses.map(a => (a, key))
  }

  def findLinkDestinations(html: String): Iterable[String] = {
    val links = Jsoup.parse(html).select("a[href]").iterator().toList
    links.map(l => l.attr("href").split("/").last)
  }

  /**
   * Reduce step of calculate a Reverse Link Graph
   * Take the result of a map function, and group
   * together to get the reverse link graph
   *
   * @param input List((Link Destination, Link Source))
   * @return List(Link Destination, List(Link Source))
   */
  def reduce(input: List[(String, String)]) = input.groupBy(f => f._1)
}