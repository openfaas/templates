FROM openfaas/classic-watchdog:0.18.1 as watchdog

FROM node:12.13.0-alpine as ship

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

RUN addgroup -S app && adduser app -S -G app

RUN apk add --no-cache nodejs nodejs-npm ca-certificates

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
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
