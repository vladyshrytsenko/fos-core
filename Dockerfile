
FROM openjdk:21-jdk
WORKDIR /app
COPY target/food-ordering-system-0.0.1-SNAPSHOT.jar food-ordering-system.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "food-ordering-system.jar"]