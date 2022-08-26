DEST=AppDir

rm -rf $DEST
mkdir $DEST

cp -r app-image/* $DEST
cp -r jar $DEST

source ~/data/repos/bash/load.sh

java-download jre 11

f=$(ls *.tar.gz)
untargz $f
mv $(targz-root $f) $DEST/jre
rm $f
