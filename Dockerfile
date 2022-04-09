FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /opt/app
COPY ./ /opt/app
RUN mvn clean install -DskipTests

FROM openjdk:17
COPY --from=build /opt/app/target/*.jar docker-jenkins-pipeline.jar
ENV PORT 8081
EXPOSE $PORT
ENTRYPOINT ["java","-jar","-Dserver.port=${PORT}","docker-jenkins-pipeline.jar"]