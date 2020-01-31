FROM openfaas/classic-watchdog:0.18.1 as watchdog
FROM golang:1.13-alpine3.11 as builder

# Allows you to add additional packages via build-arg
ARG ADDITIONAL_PACKAGE
ARG CGO_ENABLED=0
ARG GO111MODULE="off"
ARG GOPROXY=""
ARG GOFLAGS=""

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

ENV CGO_ENABLED=0

WORKDIR /go/src/handler
COPY . .

# Add user overrides to the root go.mod, which is the only place "replace" can be used
RUN cat function/GO_REPLACE.txt >> ./go.mod || exit 0

# Run a gofmt and exclude all vendored code.
RUN test -z "$(gofmt -l $(find . -type f -name '*.go' -not -path "./vendor/*" -not -path "./function/vendor/*"))" || { echo "Run \"gofmt -s -w\" on your Golang code"; exit 1; }

WORKDIR /go/src/handler/function

RUN go test ./... -cover

WORKDIR /go/src/handler

RUN CGO_ENABLED=${CGO_ENABLED} GOOS=linux \
    go build --ldflags "-s -w" -a -installsuffix cgo -o handler .

FROM alpine:3.11
RUN apk --no-cache add ca-certificates \
    && addgroup -S app && adduser -S -g app app \
    && mkdir -p /home/app \
    && chown app /home/app

WORKDIR /home/app

COPY --from=builder /usr/bin/fwatchdog         .
COPY --from=builder /go/src/handler/function/  .
COPY --from=builder /go/src/handler/handler    .

RUN chown -R app /home/app

USER app

ENV fprocess="./handler"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["./fwatchdog"]
