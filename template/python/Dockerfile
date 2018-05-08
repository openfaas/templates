FROM python:2.7-alpine

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add curl \ 
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.8.0/fwatchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

# Add non root user
RUN addgroup -S app && adduser -S -g app app
RUN chown app /home/app

USER app

WORKDIR /home/app/

COPY index.py           .
COPY requirements.txt   .
RUN pip install -r requirements.txt

RUN mkdir -p function
RUN touch ./function/__init__.py
WORKDIR /home/app/function/
COPY function/requirements.txt	.
RUN pip install --user app -r requirements.txt

WORKDIR /home/app/
COPY function           function

ENV fprocess="python index.py"

HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
