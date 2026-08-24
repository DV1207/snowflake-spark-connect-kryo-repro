ThisBuild / scalaVersion := "2.12.18"
val sparkVersion = "3.5.9"
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
assemblyMergeStrategy in assembly := { case _ => MergeStrategy.first }
