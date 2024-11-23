FROM openjdk:17-jdk
COPY target/parcial-2-elecciones-2023-0.0.1.jar elecciones.jar

ENTRYPOINT ["java", "-jar", "elecciones.jar"]

#mvn clean package -DskipTests
#docker build -t elecciones-mio .
#docker run -d --name elecciones-mvz -p 8081:8081 elecciones-mio