FROM openfaas/classic-watchdog:0.18.1 as watchdog

FROM microsoft/dotnet:2.1-sdk as builder

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

FROM microsoft/dotnet:2.1-runtime

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# Create a non-root user
RUN addgroup --system app \
    && adduser --system --ingroup app app

WORKDIR /home/app/
COPY --from=builder /home/app/src/published .
RUN chown app:app -R /home/app

USER app

ENV fprocess="dotnet ./root.dll"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
