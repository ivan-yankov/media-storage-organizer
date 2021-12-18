echo "deploy <majorVersion>.<minorVersion> <rootDir>"
dest=$2
cp target/scala-2.12/media-storage-organizer-assembly-$1.jar $dest
cp start.bat $dest
cp start.sh $dest
