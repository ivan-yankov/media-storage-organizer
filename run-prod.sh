VERSION='1.9'
DB=/media/ivan-yankov/9df430e5-1b64-45cf-8c51-42d3d37e89fe/folklore-media-storage-organizer/database

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
export JRE_HOME=$JAVA_HOME/jre

$JAVA_HOME/bin/java -Xms2G -Xmx6G -jar target/media-storage-organizer-$VERSION.jar --db-url jdbc:derby:$DB
