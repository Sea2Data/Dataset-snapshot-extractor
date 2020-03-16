# Generate dataset collection from snapshot

Prototype for generating versioned dataset collections. Queries the API for data set snapshot for specified years and mission types, with an optional date restriction.
The latest snapshot older than the date restriction will fetched for each data set. The result approximates the state of the database on a given date (snapshots are generated daily).
The snapshot system was rolled out for NMDbiotic v3 in february 2019. Attempting to set the restriction date prior to this will result in empty sets.
Output is always generated from snapshots (rather than live data). If the date restriction is not provided, data set collection will be generated from latest snapshots.

## Usages
* Versioned files for all data in a year can be obtained by including all mission types
* Extraction of fisheries dependent data in a single file can be achieved by including the mission types emplyed in fisheries dependent sampling.

## Examples
For usage instructions:  
java -jar target/DatasetSnapshotDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar 

Example for extracting a data set of all fisheries dependent data from snapshots reflecting the state of the database at 2019-02-10:  
java -jar target/DatasetSnapshotDownloader-1.0-SNAPSHOT-jar-with-dependencies.jar http://tomcat7.imr.no:8080/apis/nmdapi/biotic/v3 -Y 2018 -M 1 2 3 9 10 11 12 13 19 -D 2019-02-10 -O commSampling2018_2019_02_10.xml