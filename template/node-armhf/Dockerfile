FROM openfaas/classic-watchdog:0.15.4 as watchdog

FROM arm32v6/alpine:3.6

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

RUN apk add --no-cache nodejs nodejs-npm ca-certificates

WORKDIR /root/

COPY package.json   .

RUN npm i
COPY index.js       .
COPY function       function
WORKDIR /root/function

ENV NPM_CONFIG_LOGLEVEL warn
RUN npm i || :
WORKDIR /root/

ENV cgi_headers="true"

ENV fprocess="node index.js"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
