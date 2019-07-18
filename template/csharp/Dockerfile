FROM microsoft/aspnetcore-build:2.0 as builder

ENV DOTNET_CLI_TELEMETRY_OPTOUT 1

# Optimize for Docker builder caching by adding projects first.

RUN mkdir -p /root/src/function
WORKDIR /root/src/function
COPY ./function/FunctionHandler.csproj  .

WORKDIR /root/src/
COPY ./root.csproj  .
RUN dotnet restore ./root.csproj

COPY .  .

RUN dotnet publish -c release -o published

FROM microsoft/aspnetcore:2.0

RUN apt-get update -qy \
    && apt-get install -qy curl ca-certificates --no-install-recommends \ 
    && echo "Pulling watchdog binary from Github." \
    && curl -sSLf https://github.com/openfaas-incubator/of-watchdog/releases/download/0.2.3/of-watchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apt-get -qy remove curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /root/
COPY --from=builder /root/src/published .

ENV fprocess="dotnet ./root.dll"
ENV cgi_headers="true"
ENV mode="http"
ENV upstream_url="http://127.0.0.1"

EXPOSE 8080
CMD ["fwatchdog"]