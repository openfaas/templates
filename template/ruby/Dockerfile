FROM ruby:2.4-alpine3.6

ARG ADDITIONAL_PACKAGE

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add curl ${ADDITIONAL_PACKAGE} \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.8.0/fwatchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

WORKDIR /home/app

COPY Gemfile    		.
COPY index.rb           .
COPY function           function

RUN bundle install \
  && mkdir -p /home/app/function

WORKDIR /home/app/function

RUN bundle install 

RUN addgroup -S app \
  && adduser -S -g app app

RUN chown app:app -R /home/app

WORKDIR /home/app

ENV fprocess="ruby index.rb"

HEALTHCHECK --interval=2s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
