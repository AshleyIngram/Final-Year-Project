import AssemblyKeys._

seq(assemblySettings: _*)

name := "Nephele"

version := "1.0"

libraryDependencies += "eu.stratosphere" % "stratosphere-clients" % "0.4"

libraryDependencies += "eu.stratosphere" % "stratosphere-scala" % "0.4"

libraryDependencies += "eu.stratosphere" % "stratosphere-addons" % "0.4-hadoop2"

libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "0.20.2"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}
}