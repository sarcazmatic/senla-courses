FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar app.jar
ENV TZ=Europe/Moscow
ENTRYPOINT ["java","-jar","/app.jar"]