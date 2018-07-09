FROM arm32v6/alpine:3.6

RUN addgroup -S app && adduser app -S -G app

RUN apk add --no-cache nodejs nodejs-npm ca-certificates
RUN apk --no-cache add curl \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.8.0/fwatchdog-armhf > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

RUN mkdir -p /home/app

# Wrapper/boot-strapper
WORKDIR /home/app

COPY package.json   .

RUN npm i

WORKDIR /home/app/function

COPY index.js       .
COPY function       function

ENV NPM_CONFIG_LOGLEVEL warn
RUN npm i || :

COPY --chown=app:app function/ .

WORKDIR /home/app/

RUN chmod +rx -R ./function \
    && chown app:app -R /home/app \
    && chmod 777 /tmp

USER app

ENV cgi_headers="true"

ENV fprocess="node index.js"

HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
