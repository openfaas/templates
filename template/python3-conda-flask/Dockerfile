FROM frolvlad/alpine-miniconda3

# Allows you to add additional packages via build-arg
ARG ADDITIONAL_PACKAGE

# add non-root user
RUN addgroup -S app && adduser -S -g app app

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add curl ca-certificates musl-dev gcc make ${ADDITIONAL_PACKAGE} \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas-incubator/of-watchdog/releases/download/0.4.6/of-watchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

# precompile python
RUN python -m compileall /usr/local # precompile

USER app
ENV PATH=$PATH:/home/app/.local/bin

WORKDIR /home/app

COPY index.py           .
COPY requirements.txt   .

# install base requirements
RUN pip install --user -r requirements.txt

# install function requirements
WORKDIR /home/app/function
COPY function/requirements.txt	.
RUN pip install --user -r requirements.txt

# copy function
WORKDIR /home/app
COPY function   function

USER root
RUN touch function/__init__.py

WORKDIR /home/app/function/conda
RUN while read channel; do conda config --append channels $channel; done < channels.txt \
    && while read requirement; do conda install --yes $requirement; done < requirements.txt \
    && chown -R app:app /home/app

WORKDIR /home/app

# precompile python
USER app
RUN python -m compileall .

ENV fprocess="python index.py" \
    cgi_headers="true" \
    mode="http" \
    upstream_url="http://127.0.0.1:5000"

EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
