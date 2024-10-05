FROM openjdk:17-jdk
COPY target/parcial-1-rwc-2023-0.0.1-SNAPSHOT.jar rugby.jar

ENTRYPOINT ["java", "-jar", "rugby.jar"]

#docker build -t rugby-mvz .
#docker run -d --name rugby-mvz-container -p 8084:8084 rugby-mvz