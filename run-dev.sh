source var.sh
java -Xms2G -Xmx6G -jar target/scala-2.12/media-storage-organizer-assembly-$VERSION.jar --db-dir=$TEST_DB --media-dir=$TEST_MEDIA --tmp-dir=$TEST_TMP
