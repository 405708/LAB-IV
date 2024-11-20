FROM openjdk:17-jdk
COPY target/lciii-scaffolding-0.0.1-SNAPSHOT.jar country.jar

ENTRYPOINT ["java", "-jar", "country.jar"]

#docker build -t country-mvz .
#docker run -d --name country-mvz-container -p 8085:8085 country-mvz