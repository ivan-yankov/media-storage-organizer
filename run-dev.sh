VERSION='1.9'

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
export JRE_HOME=$JAVA_HOME/jre

$JAVA_HOME/bin/java -Xms2G -Xmx6G -jar target/media-storage-organizer-$VERSION.jar --db-url jdbc:derby:testdb
