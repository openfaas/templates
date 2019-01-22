FROM openjdk:8-jdk-alpine as builder

RUN apk --no-cache add curl \
    && addgroup -S app && adduser -S -g app app \
    && mkdir -p /opt/ && cd /opt/ \
    && echo "Downloading gradle.." \
    && curl -SfL https://services.gradle.org/distributions/gradle-4.8.1-bin.zip -o gradle-bin.zip \
    && unzip gradle-bin.zip -d /opt/ \
    && rm gradle-bin.zip \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas-incubator/of-watchdog/releases/download/0.4.6/of-watchdog-armhf > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl

# Export some environment variables
ENV GRADLE_HOME=/opt/gradle-4.8.1/
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN mkdir -p /home/app/libs

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"
WORKDIR /home/app

COPY . /home/app/

# Keep downloaded Jar files around for next build; speed up repeated builds:
VOLUME /home/app/.gradle/caches/

RUN gradle build

RUN cd /tmp; unzip /home/app/entrypoint/build/distributions/entrypoint-1.0.zip 


FROM arm32v7/openjdk:8-jre-slim as ship

# Important: You can't put any RUN commands in this section, this is an ARM image, and you probably aren't building this image on ARM, but on a PC.
# You can put RUN commands in the builder above, or add them to the init.sh script.

COPY --from=builder /usr/bin/fwatchdog /usr/bin/fwatchdog
COPY init.sh /
COPY --from=builder /tmp/entrypoint-1.0/ /home/app/entrypoint-1.0/ 

WORKDIR /home/app

HEALTHCHECK --interval=2s CMD [ -e /tmp/.lock ] || exit 1

CMD ["/init.sh"]
