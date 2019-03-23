FROM swift:4

WORKDIR /swift/src/handler

RUN mkdir -p /app
WORKDIR /app
COPY . .
RUN swiftc function/handler.swift main.swift -o handler

RUN curl -sSL https://github.com/openfaas/faas/releases/download/0.9.4/fwatchdog > ./fwatchdog
RUN chmod +x ./fwatchdog

ENV fprocess="./handler"

HEALTHCHECK --interval=2s CMD [ -e /tmp/.lock ] || exit 1

CMD ["./fwatchdog"]
