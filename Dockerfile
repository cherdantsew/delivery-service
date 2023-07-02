FROM openjdk:17
VOLUME /tmp
COPY target/*.jar deliver-app
ENTRYPOINT ["java", "-jar","deliver-app"]
EXPOSE 8082