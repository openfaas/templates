# Some portions of this code are Copyright 2019 Oath Inc. Licensed under the terms of the project license.

FROM python:3.7-alpine

# Allows you to add additional packages via build-arg
ARG ADDITIONAL_PACKAGE

# add non-root user
RUN addgroup -S app && adduser -S -g app app

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add ca-certificates ${ADDITIONAL_PACKAGE} \
    && echo "Pulling watchdog binary from Github." \
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
RUN touch function/__init__.py \
    && chown -R app:app .

# precompile python
USER app
RUN python -m compileall .

EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

CMD ["python", "index.py"]
