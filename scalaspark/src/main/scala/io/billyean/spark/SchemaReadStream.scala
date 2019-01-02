package io.billyean.spark

import io.billyean.spark.SocketStream._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

import scala.language.implicitConversions

case class Person(name:String, age: Int, zip: String, phone: String)

object SchemaReadStream extends App {
  implicit def stringToPerson(line: String): Person = {
    val array = line.split(",")

    Person(array(0), array(1).toInt, array(2), array(3))
  }

  val server = "localhost"
  val port = 12346
  val duration = 60 * 10 * 1000

  val spark = SparkSession.builder.appName("SchemaReadStream").getOrCreate

  import spark.implicits._
  val lines = spark.readStream
    .format("socket")
    .option("host", server)
    .option("port", port)
    .load()

//  val pSchema = ScalaReflection.schemaFor[Person].dataType.asInstanceOf[StructType]
    val peopleStream = lines.as[String].map(stringToPerson(_))
    println(peopleStream.schema)
    val peopleCountStream = peopleStream.groupByKey(_.zip).count()


  peopleCountStream.writeStream.outputMode("complete").format("console").start.awaitTermination(duration)
}
