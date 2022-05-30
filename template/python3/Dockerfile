# Builder stage that allows you to use git modules from private repos
FROM --platform=${TARGETPLATFORM:-linux/amd64} python:3-alpine as builder

# Basic user, python and certificate setup
ARG ADDITIONAL_PACKAGE
RUN apk --no-cache add ca-certificates ${ADDITIONAL_PACKAGE}
RUN addgroup -S app && adduser app -S -G app
WORKDIR /home/app/
RUN chown -R app /home/app && \
    mkdir -p /home/app/python && chown -R app /home/app
USER app
ENV PATH=$PATH:/home/app/.local/bin:/home/app/python/bin/
ENV PYTHONPATH=$PYTHONPATH:/home/app/python

# Token to be provided as argument
ARG GIT_TOKEN=no_token_set

# Install git and make the git token available as environment variable
USER root
RUN apk --no-cache add git

# Install template requirements
USER app
WORKDIR /home/app/
COPY requirements.txt   .
RUN GIT_TOKEN=${GIT_TOKEN} pip install -r requirements.txt --target=/home/app/python

# Install function specific requirements
RUN mkdir -p function
WORKDIR /home/app/function/
COPY function/requirements.txt	.
RUN GIT_TOKEN=${GIT_TOKEN} pip install -r requirements.txt --target=/home/app/python

FROM --platform=${TARGETPLATFORM:-linux/amd64} ghcr.io/openfaas/classic-watchdog:0.2.0 as watchdog

# Actual image
FROM --platform=${TARGETPLATFORM:-linux/amd64} python:3-alpine

# Basic user, python and certificate setup
ARG ADDITIONAL_PACKAGE
RUN apk --no-cache add ca-certificates ${ADDITIONAL_PACKAGE}
RUN addgroup -S app && adduser app -S -G app
WORKDIR /home/app/
RUN chown -R app /home/app && \
    mkdir -p /home/app/python && chown -R app /home/app
USER app
ENV PATH=$PATH:/home/app/.local/bin:/home/app/python/bin/
ENV PYTHONPATH=$PYTHONPATH:/home/app/python

# Copy over watchdog
USER root
COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# Copy over template files
USER app
WORKDIR /home/app/
COPY index.py           .
COPY requirements.txt   .

# Mark the function dir as a module
RUN mkdir -p function
RUN touch ./function/__init__.py

# Copy over the function specific requirements file
WORKDIR /home/app/function/
COPY function/requirements.txt	.

# Copy over resolved dependencies from builder stage
WORKDIR /home/app/
COPY --from=builder /home/app/.cache /home/app/.cache
COPY --from=builder /home/app/python /home/app/python

# Copy over the specific function code
USER root
COPY function           function

# Allow any user-id for OpenShift users.
RUN chown -R app:app ./ && \
    chmod -R 777 /home/app/python

# Prepare and run the watchdog
USER app
ENV fprocess="python3 index.py"
EXPOSE 8080
HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]
