echo "run <majorVersion>.<minorVersion> <rootDir>"
java -Xms2G -Xmx6G -jar target/scala-2.12/media-storage-organizer-assembly-$1.jar --db-dir=$2
