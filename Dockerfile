FROM eclipse-temurin:17-alpine
WORKDIR app
COPY /target/*.jar ./trainTicketApp.jar
ENTRYPOINT ["java", "-jar", "./trainTicketApp.jar"]