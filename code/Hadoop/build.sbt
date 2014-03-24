import AssemblyKeys._

seq(assemblySettings: _*)

name := "Hadoop"

version := "1.0"

libraryDependencies += "com.nicta" %% "scoobi" % "0.8.3"

resolvers ++= Seq(Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snaspshots"))

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}
}