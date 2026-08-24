import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

object KryoRepro {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("kryo_repro").getOrCreate()
    try {
      val enc: Encoder[Map[String, Int]] = Encoders.kryo[Map[String, Int]]
      println(s"OK: $enc")
    } catch {
      case e: Throwable =>
        println(s"FAILED: ${e.getClass.getName}: ${e.getMessage}")
    }
    spark.stop()
  }
}
