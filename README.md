Proyecto de Microservicios Prueba Tecnica

Este proyecto implementa una arquitectura de microservicios utilizando Spring Boot 3 (WebFlux), R2DBC (Base de Datos Reactiva H2 en memoria), y RabbitMQ para la comunicación asíncrona.

El sistema se compone de dos microservicios:

cliente-ms (Puerto 8081): Gestión de la información personal del cliente.
movimiento-ms (Puerto 8082): Gestión de Cuentas, Transacciones (Depósito/Retiro), y Reportes (Estado de Cuenta).

Requisitos Previos
Para levantar el proyecto localmente, debe tener instalado:
Java 17 (JDK): Versión 17 o superior.
Maven: Herramienta de construcción.
RabbitMQ Server: Necesario para el flujo de eventos asíncronos.

Configuración de RabbitMQ
El proyecto asume que RabbitMQ está corriendo en la configuración por defecto (localhost:5672, usuario guest, clave guest).
Si no tiene RabbitMQ, puede levantarlo fácilmente usando Docker:
# Levanta el contenedor de RabbitMQ
docker run -d --hostname rabbit-server --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

Levantamiento del Proyecto

Opción 1: Ejecución Directa con Maven
1. Compilación del Proyecto

Ejecute este comando desde el directorio raíz de cada microservicio (cliente-ms y movimiento-ms) para compilar el JAR y descargar las dependencias.
cd cliente-ms
mvn clean package -DskipTests

cd ../movimiento-ms
mvn clean package -DskipTests

2. ejecutar los microservicios.
   
java -jar cliente-ms/target/cliente-ms-0.0.1-SNAPSHOT.jar

java -jar movimiento-ms/target/movimiento-ms-0.0.1-SNAPSHOT.jar


Opción 2: Compilación y Ejecución con Docker

Este método compila el código dentro de la imagen de Docker y luego lo ejecuta.
1. Construcción de las Imágenes Docker

Ejecute los siguientes comandos desde el directorio raíz de cada microservicio (donde se encuentran los Dockerfile):

docker build -t banco/cliente-ms .

docker build -t banco/movimiento-ms .

2. Ejecución de los Contenedores

Ejecute los contenedores y asegúrese de mapear los puertos:

docker run -d --name cliente-ms -p 8081:8081 banco/cliente-ms

docker run -d --name movimiento-ms -p 8082:8082 banco/movimiento-ms

# Ejecutar todas las pruebas (Unitaria y de Integración)
mvn test

Ejecutar pruebas de aceptación con Karate
1. Ir al directorio de Karate (puede estar dentro de cliente-ms/karate):
cd cliente-ms/karate

2. Ejecutar las pruebas:
gradlew test --info
Todas las pruebas de aceptación están definidas en classpath:karate/features/cliente/cliente-crud.feature. Se validan los flujos de crear, actualizar, obtener y eliminar clientes.
