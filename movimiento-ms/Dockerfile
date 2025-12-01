FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/movimiento-ms-0.0.1-SNAPSHOT.jar movimiento-ms.jar
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "movimiento-ms.jar"]