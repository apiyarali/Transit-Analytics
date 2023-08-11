# Public Transit Data Analysis for Heat Map Visualization

This project aims to conduct an analysis of public transit data within a designated urban area, that is chosen by the user. The central objective is to compile a comprehensive CSV dataset that can be harnessed to generate a heatmap showcasing transit stops with the highest trip frequencies.

Urban centers' public transportation agencies routinely release their transit data in a standardized format referred to as the General Transit Feed Specification (GTFS). Our project will focus on the GTFS schedule variant, commonly known as GTFS static data.

The resultant dataset holds potential for streamlining various critical aspects of resource allocation, operational planning, and service provision within transit agencies. Moreover, it offers a means to identify population hubs, establish crowd management standards, implement supplementary peak-hour services, trial new route options, and more. For instance, by correlating the heatmap of frequently visited stops with census data regarding population clusters, we can ensure the delivery of dependable transit services. Similarly, overlaying the transit heatmap with city planning data can shed light on upcoming educational institutions or commercial zones, facilitating preemptive adjustments to transit strategies.

This application has been developed using the Scala programming language in conjunction with Spark Batch Processing. Given the non-real-time nature of the data and an estimated dataset size ranging from 8 to 12 million rows, Spark's Dataset framework is employed for handling the analysis. Each data file corresponds to dedicated Scala Objects, ensuring optimal organization and processing. The data aggregation process hinges on Spark SQL, leveraging inner joins to synthesize meaningful insights from the dataset's various dimensions.

This project is to analyze a city’s (selected or defined by user) public transit data to generate an aggregate dataset which can be used to visualize a heat map of stops with most trips.

# How to Run

Install Docker

In project root,

1. Pull docker image
```
docker pull hseeberger/scala-sbt:eclipse-temurin-11.0.14.1_1.6.2_2.13.8
```

2. Run a terminal in docker
```
docker run -v ${PWD}:/opt/projects/2022-transitAnalytics -it --rm hseeberger/scala-sbt:eclipse-temurin-11.0.14.1_1.6.2_2.13.8 /bin/bash
```

3. cd to the mounted volume

```
cd /opt/projects/2022-transitAnalytics
```

4. Run Spark application
```
sbt "runMain org.transitAnalytics.gtfs.GtfsDSApplication *[Your STATIC GTFS ZIP Link]*"

Example: *sbt "runMain org.transitAnalytics.gtfs.GtfsDSApplication https://ckan0.cf.opendata.inter.prod-toronto.ca/dataset/7795b45e-e65a-4465-81fc-c36b9dfff169/resource/cfb6b2b8-6191-41e3-bda1-b175c51148cb/download/TTC%20Routes%20and%20Schedules%20Data.zip"*
```

5. Download the CSV Dataset

```
Full Dataset:

cd /opt/spark/heat

docker cp [Your Docker ID]:/opt/spark/heat/ .

Example: docker cp 09c5078c2682:/opt/spark/heat/ .

Sample Dataset for quick analysis:

cd /opt/spark/heat/sample
docker cp [Your Docker ID]:/opt/spark/heat/sample/ .

Example: docker cp 09c5078c2682:/opt/spark/heat/sample/ .
```

6. Exit shell

```
exit
```
# CSV file

The CSV file should have the following columns: stop_id, stop_name, stop_latitude, and stop_longitude.

This CSV file can be imported into any visualization application to visualize the data.

Here is an example of visualization using Microsoft Excel 3D Map:

!["Heat Map"](https://github.com/apiyarali/Transit-Analytics/blob/main/HeatMap.png "Heat Map")

HeatMap
