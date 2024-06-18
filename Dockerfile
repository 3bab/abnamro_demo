FROM eclipse-temurin:21.0.2_13-jre-alpine
ADD bank-0.0.1.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/demo.jar"]