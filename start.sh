./jre-11.0.9.1-linux/bin/java -Xms2G -Xmx6G -jar \
-p javafx-linux/javafx-sdk-11.0.2/lib \
--add-modules javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.web \
--add-opens javafx.graphics/javafx.scene=ALL-UNNAMED \
--add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED \
media-storage-organizer-assembly-3.2.jar --db-dir=./database
