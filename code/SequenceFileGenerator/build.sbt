import AssemblyKeys._
seq(assemblySettings: _*)

name := "SequenceFileGenerator"

version := "1.0"

libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "0.20.2"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", "MANIFEST.MF", xs @ _*) =>
MergeStrategy.discard
    case _ => MergeStrategy.first
  }
}

