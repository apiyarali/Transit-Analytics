package org.transitAnalytics.gtfs

import org.apache.spark.sql.SparkSession
import com.typesafe.scalalogging.{LazyLogging}
import org.transitAnalytics.config.{ConfigUtils}
import org.transitAnalytics.utils.{SparkUtils}
import org.apache.spark.sql.{Dataset, DataFrame}
import pureconfig.generic.auto._

import java.io.{IOException, FileOutputStream, FileInputStream, File}
import java.util.zip.{ZipEntry, ZipInputStream}
import java.net.{URL}
import org.apache.commons.io.FileUtils
import scala.util.{Try}


// case class GTFSConfig()
case class GTFSConfig(
  name: String,
  masterUrl: String,
  zipPath: String,
  unzipPath: String,
  stopsPath: String,
  stopTimesPath: String,
  tripsPath: String,
  calendarPath: String,
  heatDataPath: String,
  heatDataSamplePath: String
)

// run with: sbt "runMain org.transitAnalytics.gtfs.GtfsDSApplication"
object GtfsDSApplication {

  // application main entry point
  def main(args: Array[String]): Unit = {

    // Read GTFS Configuration
    implicit val conf:GTFSConfig = readGTFSConfig()

    // Create Spark Session
    val spark = SparkUtils.sparkSession(conf.name, conf.masterUrl)
  
    // GTFS zip data url for Toronto, ON. Canada
    // val url = "https://ckan0.cf.opendata.inter.prod-toronto.ca/dataset/7795b45e-e65a-4465-81fc-c36b9dfff169/resource/cfb6b2b8-6191-41e3-bda1-b175c51148cb/download/TTC%20Routes%20and%20Schedules%20Data.zip"
    val url = args(0)

    // save the url zip file to temp directory
    saveZip(url)

    // unzip the url zip file to temp directory
    unZipIt(conf.zipPath, conf.unzipPath)
    
    // load unzip file data to gtfs dataset defined object (case class)
    val stopsDS = loadStops(spark)
    val stopTimesDS = loadStopTimes(spark)
    val tripsDS = loadTrips(spark)
    val calendarDS = loadCalendar(spark)

    // join the dataset to create data for heat map
    val heatDF = createHeatDF(spark, stopsDS, stopTimesDS, tripsDS, calendarDS)

    // save heatmap generated data to csv file to be use for graphical analysis
    saveHeatDF(spark, heatDF)
    
    // Delete zip folder
    FileUtils.deleteDirectory(new File("/opt/spark/tmpZip"));

    // Delete Unzip folder
    FileUtils.deleteDirectory(new File(conf.unzipPath));

    // stop the spark session
    spark.stop()
  }

  // Reference:
  // Code for saveZip is taken from: 
  // https://docs.databricks.com/_static/notebooks/zip-files-scala.html
  def saveZip(url: String)(implicit conf: GTFSConfig): Unit = {
    FileUtils.copyURLToFile(new URL(url), new File(conf.zipPath))
  }

  // Reference:
  // Code for unZipIt is taken from: 
  // https://stackoverflow.com/a/30642526
  def unZipIt(zipFile: String, outputFolder: String): Unit = {
    val buffer = new Array[Byte](1024)
    try {
      val folder = new File(outputFolder);
      if (!folder.exists()) {
        folder.mkdir();
      }

      //zip file content
      val zis: ZipInputStream = new ZipInputStream(new FileInputStream(zipFile));
      //get the zipped file list entry
      var ze: ZipEntry = zis.getNextEntry();

      while (ze != null) { 
        val fileName = ze.getName();
        val newFile = new File(outputFolder + File.separator + fileName);
        System.out.println("file unzip : " + newFile.getAbsoluteFile());

        //create folders
        new File(newFile.getParent()).mkdirs();

        val fos = new FileOutputStream(newFile);
        var len: Int = zis.read(buffer);

        while (len > 0) {
          fos.write(buffer, 0, len)
          len = zis.read(buffer)
        }

        fos.close()
        ze = zis.getNextEntry()
      }
      zis.closeEntry()
      zis.close()
    } catch {
      case e: IOException => println("exception caught: " + e.getMessage)
    }
  }

  // read GTFS configuraion
  def readGTFSConfig(): GTFSConfig = 
    ConfigUtils.loadAppConfig[GTFSConfig]{"org.transitAnalytics.gtfs-application"}
  
  // load stops.txt data to spark dataset 
  def loadStops(spark: SparkSession)(implicit conf: GTFSConfig): Dataset[Stops] = {
    import spark.implicits._
    spark
      .read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(conf.stopsPath)
      .as[Stops]
  }

  // load stop_times.txt data to spark dataset 
  def loadStopTimes(spark: SparkSession)(implicit conf: GTFSConfig): Dataset[StopTimes] = {
    import spark.implicits._
    spark
      .read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(conf.stopTimesPath)
      .as[StopTimes]
  }

  // load trips.txt data to spark dataset 
  def loadTrips(spark: SparkSession)(implicit conf: GTFSConfig): Dataset[Trips] = {
    import spark.implicits._
    spark
      .read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(conf.tripsPath) 
      .as[Trips]
  }

  // load calendar.txt data to spark dataset 
  def loadCalendar(spark: SparkSession)(implicit conf: GTFSConfig): Dataset[Calendar] = {
    import spark.implicits._
    spark
      .read
      .format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(conf.calendarPath) 
      .as[Calendar]
  }
  
  // join the dataset to create a heat map
  def createHeatDF(
    spark: SparkSession,
    stopsDS: Dataset[Stops],
    stopTimesDS: Dataset[StopTimes],
    tripsDS: Dataset[Trips],
    calendarDS: Dataset[Calendar]
  ) (implicit conf: GTFSConfig): DataFrame = {
      import spark.implicits._
      val stopJoin = stopTimesDS.join(stopsDS, "stop_id")
      val stopTripJoin = stopJoin.join(tripsDS, "trip_id")
      val stopTripCalendarJoin = stopTripJoin.join(calendarDS, "service_id")
      stopTripCalendarJoin.select("stop_id", "stop_name", "stop_lat", "stop_lon")
  }

  // write the heat map data to csv file
  def saveHeatDF(spark: SparkSession, heatDF: DataFrame) (implicit conf: GTFSConfig): Unit = {
    import spark.implicits._
    heatDF
    .coalesce(1)
    .write
    .format("csv")
    .option("header", "true")
    .mode("overwrite")
    .save(conf.heatDataPath)

    // Random sample of approximately 100,000 rows to do quick random sampling analysis and
    // visualization
    val heatDFSample = heatDF.sample(0.023)
    heatDFSample
    .coalesce(1)
    .write
    .format("csv")
    .option("header", "true")
    .mode("overwrite")
    .save(conf.heatDataSamplePath)
  }
}