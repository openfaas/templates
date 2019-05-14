FROM python:3-alpine

# Allows you to add additional packages via build-arg
ARG ADDITIONAL_PACKAGE

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add curl ${ADDITIONAL_PACKAGE} \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.13.0/fwatchdog-armhf > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

# Add non root user
RUN addgroup -S app && adduser app -S -G app

WORKDIR /home/app/

COPY index.py           .
COPY requirements.txt   .

RUN chown -R app /home/app && \
  mkdir -p /home/app/py && chown -R app /home/app
USER app
ENV PATH=$PATH:/home/app/.local/bin:/home/app/python/bin/
ENV PYTHONPATH=$PYTHONPATH:/home/app/python

RUN pip install -r requirements.txt --target=/home/app/python

RUN mkdir -p function
RUN touch ./function/__init__.py

WORKDIR /home/app/function/
COPY function/requirements.txt	.

RUN pip install -r requirements.txt --target=/home/app/python

WORKDIR /home/app/

USER root

COPY function           function

RUN chown -R app:app ./ && \
  chmod -R 777 /home/app/python

USER app

ENV fprocess="python3 index.py"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
