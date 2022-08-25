DEST=AppDir
JRE=~/data/install/java/jre-11

rm -rf $DEST
mkdir $DEST
cp -r app-image/* $DEST
mkdir $DEST/jre
cp -r $JRE/* $DEST/jre
cp -r jar $DEST
