VERSION='2.0'
DB=testdb

java -Xms2G -Xmx6G -jar target/scala-2.12/media-storage-organizer-assembly-$VERSION.jar #--db-url jdbc:derby:$DB
