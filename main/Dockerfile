FROM openjdk:8-jdk-alpine
ADD /target/main-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=docker"]