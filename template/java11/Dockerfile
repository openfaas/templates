FROM adoptopenjdk/openjdk11:alpine-slim as builder

ENV GRADLE_VER=6.3
RUN apk update -q \
  && apk add --no-cache -q \
   curl \
   ca-certificates \
   unzip

RUN mkdir -p /opt/ && cd /opt/ \
    && echo "Downloading gradle.." \
    && curl -sSfL "https://services.gradle.org/distributions/gradle-${GRADLE_VER}-bin.zip" -o gradle-$GRADLE_VER-bin.zip \
    && unzip -q gradle-$GRADLE_VER-bin.zip -d /opt/ \
    && rm gradle-$GRADLE_VER-bin.zip

# Export some environment variables
ENV GRADLE_HOME=/opt/gradle-$GRADLE_VER/
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN mkdir -p /home/app/libs

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.parallel -Dorg.gradle.configureondemand"
WORKDIR /home/app

COPY . /home/app/

RUN gradle build
RUN find .

FROM openfaas/of-watchdog:0.7.6 as watchdog
FROM adoptopenjdk/openjdk11:alpine-jre as ship
RUN apk update -q \
  && apk add --no-cache -q \
   unzip

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

WORKDIR /home/app
COPY --from=builder /home/app/entrypoint/build/distributions/entrypoint-1.0.zip ./entrypoint-1.0.zip
RUN unzip ./entrypoint-1.0.zip

RUN addgroup --system app \
    && adduser --system --ingroup app app

WORKDIR /home/app/

RUN apk del -q unzip

USER app

ENV upstream_url="http://127.0.0.1:8082"
ENV mode="http"
ENV CLASSPATH="/home/app/entrypoint-1.0/lib/*"

#RUN java -Xshare:dump \
#             -XX:SharedClassListFile=classes.lst \
#             -XX:SharedArchiveFile=app-cds.jsa \
#             --class-path app.jar

ENV fprocess="java -noverify -XX:TieredStopAtLevel=1 -XX:CICompilerCount=1 -XX:+UseSerialGC -XX:+UnlockExperimentalVMOptions -XX:-UsePerfData -XX:+UseContainerSupport -XX:SharedArchiveFile=app-cds.jsa -jar app.jar"
EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
