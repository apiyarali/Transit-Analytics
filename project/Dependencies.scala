import sbt._

object Dependencies {
  lazy val scalaTest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    "org.scalacheck" %% "scalacheck" % "1.15.4",
    // for law testing cats based typeclasses
    "org.typelevel" %% "cats-laws" % "2.0.0" % Test,
    "org.typelevel" %% "cats-testkit-scalatest" % "2.1.5"% Test,
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.15" % "1.3.0" % Test
  )

  val circeVersion = "0.13.0"
  val pureconfigVersion = "0.15.0"
  val catsVersion = "2.2.0"
  val sparkVersion = "3.2.1"
  val AkkaVersion = "2.6.19"
  val AlpakkaKafkaVersion = "2.1.0"

  lazy val core = Seq(
    // cats FP libary
    "org.typelevel" %% "cats-core" % catsVersion,

    // support for JSON formats
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,

    // support for typesafe configuration
    "com.github.pureconfig" %% "pureconfig" % pureconfigVersion,

    // Algebird
    "com.twitter" % "algebird-core_2.13" % "0.13.9",
    
    // parallel collections
    "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",

    // spark
    // "org.apache.spark" %% "spark-sql" % sparkVersion % Provided, // for submiting spark app as a job to cluster
    "org.apache.spark" %% "spark-sql" % sparkVersion, // for simple standalone spark app

    // // akka streams
    // "com.typesafe.akka" %% "akka-stream" % AkkaVersion,

    // // alpakka kafka
    // "com.typesafe.akka" %% "akka-stream-kafka" % AlpakkaKafkaVersion,

    // // kafka streams
    // "org.apache.kafka" %% "kafka-streams-scala" % "2.7.0",
    // "com.goyeau" %% "kafka-streams-circe" % "0.6.3",

    // logging
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3",

    // Elasticsearch
    // https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-spark-30
    "org.elasticsearch" %% "elasticsearch-spark-30" % "8.2.0" excludeAll ExclusionRule(organization = "org.apache.spark"),
    // "org.elasticsearch" %% "elasticsearch" % "7.0.0"
    // "org.elasticsearch" %% "elasticsearch-spark-20_2.11" % "5.3.1"

    // "org.elasticsearch" % "elasticsearch-hadoop" % "8.2.0"

    // "org.apache.hadoop" % "hadoop-common" % "3.3.2"

    "commons-httpclient" % "commons-httpclient" % "3.1"

  )
}
