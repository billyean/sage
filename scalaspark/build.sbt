name := "scalaspark"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "4.0",
  "org.apache.spark" % "spark-streaming_2.11" % "2.4.0",
  "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.4.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.4.0"
)


//lazy val className = TaskKey[String]("class name")
//import complete.DefaultParsers._
//className := {
//  val args: Seq[String] = spaceDelimited("<arg>").parsed
//  args.size match {
//    case 0 => "io.billyean.spark.SocketStream"
//    case _ => args.head
//  }
//}

val sparkRun = taskKey[Unit]("Submit jar to spark.")
sparkRun := {
  val pb = (artifactPath in (Compile, packageBin)).value

  import scala.sys.process._
  //  println(className.value)
  println(pb.getPath)
  ("spark-submit --class io.billyean.spark.SchemaReadStream --master local[4] " + pb.getPath)!
}