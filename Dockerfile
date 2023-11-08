FROM openjdk:17.0.2-jdk
COPY target/app.jar /usr/local/service/
ENTRYPOINT ["java", "-jar", "-Dliquibase.hub.mode=off", "/usr/local/service/app.jar"]
