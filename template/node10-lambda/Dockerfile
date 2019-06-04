FROM node:10.12.0-alpine

RUN addgroup -S app && adduser -S -g app app

WORKDIR /home/app/

# Turn down the verbosity to default level.
ENV NPM_CONFIG_LOGLEVEL warn

# WORKDIR /home/app
COPY package.json ./

# This ordering means the npm installation is cached for the outer function handler.
RUN npm i

# Copy outer function handler
WORKDIR /home/app/function
COPY function/ ./

# Set correct permissions to use non root user
WORKDIR /home/app/

ENV shim_port=9000
ENV port=8080
ENV AWS_LAMBDA_FUNCTION_NAME=openfaas
ENV AWS_LAMBDA_RUNTIME_API=127.0.0.1:9000
ENV AWS_LAMBDA_FUNCTION_VERSION=0.1
ENV AWS_LAMBDA_FUNCTION_MEMORY_SIZE=128MB
ENV LAMBDA_TASK_ROOT=/home/app/function
ENV _HANDLER=".handler"

COPY entrypoint.sh .
COPY lambda-shim .
COPY bootstrap.js .

# chmod for tmp is for a buildkit issue (@alexellis)
RUN chown app:app -R /home/app \
    && chmod 777 /tmp

USER app
EXPOSE 8080

#HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["./entrypoint.sh"]

