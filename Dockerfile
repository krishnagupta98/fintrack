FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

RUN apk add --no-cache pgbouncer redis && \
    addgroup -S postgres && adduser -S postgres -G postgres

COPY --from=build /app/target/*.jar app.jar
COPY pgbouncer.ini /etc/pgbouncer/pgbouncer.ini

RUN chown -R postgres:postgres /etc/pgbouncer && \
    chmod 600 /etc/pgbouncer/pgbouncer.ini

ENTRYPOINT ["/bin/sh", "-c", \
    "redis-server --daemonize yes --maxmemory 512mb --maxmemory-policy allkeys-lru && \
    su postgres -s /bin/sh -c 'pgbouncer -d /etc/pgbouncer/pgbouncer.ini' && \
    java -Xms512m -Xmx2g -jar app.jar"]