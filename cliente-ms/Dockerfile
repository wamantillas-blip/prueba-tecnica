FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/cliente-ms-0.0.1-SNAPSHOT.jar cliente-ms.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "cliente-ms.jar"]