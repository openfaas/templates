FROM openfaas/classic-watchdog:0.18.1 as watchdog

FROM ruby:2.7-alpine3.11

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

ARG ADDITIONAL_PACKAGE

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add ${ADDITIONAL_PACKAGE}

WORKDIR /home/app

COPY Gemfile   .
COPY index.rb  .
COPY function  function

RUN bundle install \
  && mkdir -p /home/app/function

WORKDIR /home/app/function

RUN bundle install

RUN addgroup -S app \
  && adduser app -S -G app

RUN chown app:app -R /home/app

USER app

WORKDIR /home/app

ENV fprocess="ruby index.rb"
EXPOSE 8080

HEALTHCHECK --interval=2s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
