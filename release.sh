rm -rf jar

./build.sh

sbt incrementVersion
git add version.txt
git commit -m "Increment version"

sbt package collectDependencies
