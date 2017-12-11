FROM java:8
MAINTAINER oxchains
ADD target/themis-eureka-1.0.1-SNAPSHOT.jar app.jar
EXPOSE 9010
ENTRYPOINT ["/usr/bin/java","-jar","app.jar"]