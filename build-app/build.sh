image=build-app
src=src
testdb=test-db-unit
pom=pom.xml

cp -r ../$src .
cp -r ../$testdb .
cp ../$pom .

docker build --tag $image .

rm -rf ./$src
rm -rf ./$testdb
rm -rf ./$pom

vol=build-app-vol

docker volume create $vol

docker run -v $vol:/home/developer/project $image

mkdir -p ../target
rm -rf ../target/*
cp -r /home/yankov/docker/volumes/$vol/_data/target ../
