# Public Transit Data Analysis for Heat Map Visualization

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

docker cp *[Your Docker ID]*:/opt/spark/heat/ .
Example: *docker cp 09c5078c2682:/opt/spark/heat/ .*

Sample Dataset for quick analysis:

cd /opt/spark/heat/sample
docker cp *[Your Docker ID]*:/opt/spark/heat/sample/ .
Example: *docker cp 09c5078c2682:/opt/spark/heat/sample/ .*
```

6. Exit shell

```
exit
```
