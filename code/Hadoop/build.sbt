name := "Hadoop"

version := "1.0"

libraryDependencies += "com.nicta" %% "scoobi" % "0.8.3"

resolvers ++= Seq(Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snaspshots"))
