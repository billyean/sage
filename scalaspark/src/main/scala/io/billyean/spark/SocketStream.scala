package io.billyean.spark

import org.apache.spark.sql.SparkSession

object SocketStream extends App {
  val server = "localhost"
  val port = 12345
  val duration = 60000

  val spark = SparkSession.builder.appName("SocketStream").getOrCreate

  import spark.implicits._
  val lines = spark.readStream
                .format("socket")
                .option("host", server)
                .option("port", port)
                .load()

  val words = lines.as[String].flatMap(_.split("\\s")).groupByKey(_.toLowerCase).count().orderBy($"count(1)" desc)

  words.writeStream.outputMode("complete").format("console").start.awaitTermination(duration)
}
