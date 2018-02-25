FROM openjdk:8-jre-slim

# Install ammonite for scala 2.11 and fwatchdog

RUN apt-get update -qy \
    && apt-get install -qy curl ca-certificates --no-install-recommends \ 
    && curl -L -o /bin/amm https://git.io/vdNvV && chmod +x /bin/amm \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.6.9/fwatchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apt-get -qy remove curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# COPY predef.sc /root/.ammonite/predef.sc
COPY Main.sc /root/Main.sc
COPY function/Function.sc /root/function/Function.sc

WORKDIR /root/

ENV fprocess="amm -s /root/Main.sc"
EXPOSE 8080
CMD ["fwatchdog"]

