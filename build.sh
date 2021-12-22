rm -rf jar
sbt clean compile test package collectDependencies
