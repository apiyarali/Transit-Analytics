package org.transitAnalytics.gtfs

// Case classes and companion objects for each csv text file in the GTFS
// General Transit Feed Specification data

// stops.txt – this file contains stop id, stop longitude, stop latitude, type of stop and parent location.
final case class Stops(
  stop_id: String,
  stop_code: Int,
  stop_name: String,
  stop_desc: String,
  stop_lat: Double,
  stop_lon: Double,
  zone_id: String,
  stop_url: String,
  location_type: String,
  parent_station: String,
  stop_timezone: String,
  wheelchair_boarding: String,
)

// shapes.txt – this file contains geo location of neighborhood boundaries. 
final case class Shapes(
  shape_id: String,
  shape_pt_lat: Double,
  shape_pt_lon: Double, 
  shape_pt_sequence: Int,
  shape_dist_traveled: Double
)

// stop_times.txt – this file contains trip id, stop id, arrival times and departure times 
// (for example trip x at stop 1A will be arriving at 6 am, departing at 6.02 am trip x at 
// stop 1B will be arriving at 6.10 am, departing at 6.12 am).
final case class StopTimes(
  trip_id: String,
  arrival_time: String,
  departure_time: String,
  stop_id: String, 
  stop_sequence: Int, 
  stop_headsign: String,
  pickup_type: Int,
  drop_off_type: Int,
  shape_dist_traveled: Double
){
  // Getting departure hour from time stamp string (HH:MM:SS)
  def dept_hour: String = {
    arrival_time.split(":")(0)
  }
  // Getting arrival hour from time stamp string (HH:MM:SS)
  def arriv_hour: String = {
    arrival_time.split(":")(0)
  }
}

// trips.txt – this file contains route id, service id, trip id and 
// where the trip is heading (for example heading downtown).
final case class Trips(
  route_id: String,
  service_id: String,
  trip_id: String,
  trip_headsign: String,
  trip_short_name: String,
  direction_id: Int,
  block_id: String,
  shape_id: String,
  wheelchair_accessible: Int,
  bikes_allowed: Int
)

// calendar.txt – this file contains service id, start date, end date and 
// which weekdays service is available.
final case class Calendar(
  service_id: String,
  monday: Int,
  tuesday: Int,
  wednesday: Int,
  thursday: Int,
  friday: Int,
  saturday: Int,
  sunday: Int,
  start_date: String,
  end_date: String
)

// stops.txt – this file contains stop id, stop longitude, stop latitude, 
// type of stop and parent location.
object Stops {
  def apply(csvRow: String): Stops = {
    val fields = csvRow.split(",")
    Stops(
      stop_id = fields(0),
      stop_code = fields(1).toInt,
      stop_name = fields(2),
      stop_desc = fields(3),
      stop_lat = fields(4).toDouble,
      stop_lon = fields(5).toDouble,
      zone_id = fields(6),
      stop_url = fields(7),
      location_type = fields(8),
      parent_station = fields(9),
      stop_timezone = fields(10),
      wheelchair_boarding = fields(11)
    )
  }
}

object StopTimes {
  def apply(csvRow: String): StopTimes = {
    val fields = csvRow.split(",")
    StopTimes(
      trip_id = fields(0),
      arrival_time = fields(1),
      departure_time = fields(2),
      stop_id = fields(3), 
      stop_sequence = fields(4).toInt, 
      stop_headsign = fields(5),
      pickup_type = fields(6).toInt,
      drop_off_type = fields(7).toInt,
      shape_dist_traveled = fields(8).toDouble
    )
  }
}

object Trips {
  def apply(csvRow: String): Trips = {
    val fields = csvRow.split(",")
    Trips(
      route_id = fields(0),
      service_id = fields(1),
      trip_id = fields(2),
      trip_headsign = fields(3),
      trip_short_name = fields(4),
      direction_id = fields(5).toInt,
      block_id = fields(6),
      shape_id = fields(7),
      wheelchair_accessible = fields(8).toInt,
      bikes_allowed = fields(9).toInt
    )
  }
}

object Calendar {
  def apply(csvRow: String): Calendar = {
    val fields = csvRow.split(",")
    Calendar(
      service_id = fields(0),
      monday = fields(1).toInt,
      tuesday = fields(2).toInt,
      wednesday = fields(3).toInt,
      thursday = fields(4).toInt,
      friday = fields(5).toInt,
      saturday = fields(6).toInt,
      sunday = fields(7).toInt,
      start_date = fields(8),
      end_date = fields(9)
    )
  }
}