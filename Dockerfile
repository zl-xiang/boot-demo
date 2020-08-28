#Dockerfile
FROM openjdk:8 AS jdk
ARG JAR_DIR=target/*.jar
#creating work directory
WORKDIR /web-api
EXPOSE 8080
#mounting a volume provided by host machine
VOLUME /src

RUN groupadd jenkins && \ useradd -ms /bin/bash jenkins -g jenkins

USER jenkins:jenkins

ADD ["controller/${JAR_DIR}","web-api.jar"]

ENTRYPOINT ["java","-jar","web-api.jar"]




