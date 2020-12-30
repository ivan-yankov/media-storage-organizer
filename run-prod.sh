source var.sh
java -Xms2G -Xmx6G -jar target/scala-2.12/media-storage-organizer-assembly-$VERSION.jar --db-dir=$PROD_DB --media-dir=$PROD_MEDIA --tmp-dir=$PROD_TMP
