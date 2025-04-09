FROM openjdk:18-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} animal-api-data-fetcher.jar
ENTRYPOINT ["java","-jar","/animal-api-data-fetcher.jar"]
