#
# 1. BUILD STAGE
#
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /opt/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean install -DskipTests

RUN cp target/*.jar app.jar


#
# 2. RUNTIME STAGE
#
FROM eclipse-temurin:21-jre

WORKDIR /opt/app

COPY --from=builder /opt/app/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

