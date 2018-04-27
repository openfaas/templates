FROM microsoft/dotnet:2.0-sdk as builder

# Supress collection of data.
ENV DOTNET_CLI_TELEMETRY_OPTOUT 1

# Optimize for Docker builder caching by adding projects first.

RUN mkdir -p /home/app/src/function
WORKDIR /home/app/src/function
COPY ./function/Function.csproj  .

WORKDIR /home/app/src/
COPY ./root.csproj  .
RUN dotnet restore ./root.csproj

COPY .  .

RUN dotnet publish -c release -o published

FROM microsoft/dotnet:2.0-runtime

# Create a non-root user
RUN addgroup --system app \
    && adduser --system --ingroup app app \
    && apt-get update -qy \
    && apt-get install -qy \
       curl ca-certificates --no-install-recommends \ 
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.8.0/fwatchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apt-get -qy remove curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /home/app/
COPY --from=builder /home/app/src/published .

RUN chown app:app -R /home/app

USER app

ENV fprocess="dotnet ./root.dll"
EXPOSE 8080

HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
