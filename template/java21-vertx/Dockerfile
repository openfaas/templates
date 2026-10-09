FROM eclipse-temurin:21-jdk-noble AS build
WORKDIR /home/app

COPY gradlew gradle.properties settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --version

COPY function ./function
COPY src ./src
RUN ./gradlew build installDist

FROM ghcr.io/openfaas/of-watchdog:0.12.4 AS watchdog
FROM eclipse-temurin:21-jre-noble AS ship

RUN groupadd --gid 10001 app \
    && useradd --uid 10001 --gid app --create-home app \
    && chmod 755 /home/app

WORKDIR /home/app
COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
COPY --from=build /home/app/build/install/java21-vertx/lib ./lib

USER 10001:10001

ENV mode="http" \
    upstream_url="http://127.0.0.1:8082" \
    fprocess="java -cp /home/app/lib/* com.openfaas.entrypoint.App" \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError -Dvertx.cacheDirBase=/tmp/.vertx"

EXPOSE 8080
HEALTHCHECK --interval=5s --timeout=2s --start-period=10s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]
