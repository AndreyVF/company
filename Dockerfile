FROM openjdk:14
ARG JAR_FILE=build/libs/demo.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
