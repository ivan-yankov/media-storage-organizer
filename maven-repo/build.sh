image=maven-repo
maven_repo=.m2

cp -r /home/yankov/$maven_repo .

docker build --tag $image .

rm -rf ./$maven_repo

docker save -o $image.tar $image