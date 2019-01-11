FROM maven-repo

WORKDIR /

ADD ./src ${PROJECT}/src
ADD ./test-db-unit ${PROJECT}/test-db-unit
ADD ./pom.xml ${PROJECT}

WORKDIR ${PROJECT}

CMD ${MVN}/mvn -o clean compile test package
