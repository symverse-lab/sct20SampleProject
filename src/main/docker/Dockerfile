FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} sct20sample-spring-boot-docker-0.1.0.jar
ENTRYPOINT [ "java","-jar","/app/build/libs/sct20sample-spring-boot-docker-0.1.0.jar" ]