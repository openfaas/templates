FROM openfaas/classic-watchdog:0.18.1 as watchdog

FROM mcr.microsoft.com/dotnet/core/sdk:2.2.203-stretch-arm32v7 as builder

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

RUN dotnet publish -c release -o published -r linux-arm

FROM mcr.microsoft.com/dotnet/core/runtime:2.2.4-stretch-slim-arm32v7

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# Create a non-root user
RUN addgroup --system app \
    && adduser --system --ingroup app app

WORKDIR /home/app/
COPY --from=builder /home/app/src/published .
RUN chown app:app -R /home/app

RUN apt-get update && apt-get install -qy libunwind8

USER app

ENV fprocess="dotnet ./root.dll"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]
