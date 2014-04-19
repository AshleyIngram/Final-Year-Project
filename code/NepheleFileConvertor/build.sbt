import AssemblyKeys._

seq(assemblySettings: _*)

name := "NepheleFileConvertor"

version := "1.0"

libraryDependencies += "com.github.tototoshi" % "scala-csv_2.10" % "1.0.0"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}
}