import AssemblyKeys._

seq(assemblySettings: _*)

name := "Core"

version := "1.0"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case _ => MergeStrategy.first
  }
}