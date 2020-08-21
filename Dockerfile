#Dockerfile
FROM openjdk:8 AS jdk
ARG JAR_DIR=target/*.jar
WORKDIR /src

COPY ./common/${JAR_DIR} src/common
COPY ./controller/${JAR_DIR} src/controller
COPY ./persistence/${JAR_DIR} src/persistence
COPY ./service/${JAR_DIR} src/service




