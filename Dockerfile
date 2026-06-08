FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
RUN apk add --no-cache pgbouncer
COPY --from=build /app/target/*.jar app.jar
COPY pgbouncer.ini /etc/pgbouncer/pgbouncer.ini
ENTRYPOINT ["/bin/sh", "-c", "pgbouncer -d /etc/pgbouncer/pgbouncer.ini && java -Xms512m -Xmx2g -jar app.jar"]