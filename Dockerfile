FROM amazoncorretto:17
LABEL maintainer="jsm5315@ajou.ac.kr"

ARG JAR_FILE=build/libs/spring-0.0.1-SNAPSHOT.jar

WORKDIR /home/java/acha-cha

COPY ${JAR_FILE} /home/java/acha-cha/acha-cha-server.jar

EXPOSE 7001

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker","/home/java/acha-cha/acha-cha-server.jar"]