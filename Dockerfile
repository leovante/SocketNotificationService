FROM openjdk:11.0.9-jre
COPY target/app.jar /usr/local/service/
ENTRYPOINT ["java", "-jar", "-Dliquibase.hub.mode=off", "/usr/local/service/app.jar"]
#dev2
