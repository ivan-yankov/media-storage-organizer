source var.sh
java -Xms2G -Xmx6G -jar target/scala-2.12/media-storage-organizer-assembly-$VERSION.jar $PROD_DB $PROD_MEDIA
