#!/bin/bash

set -xe # Show output on the logs

version="$1" # Get version tag
version="${version#v}" # remove the first `v` char

read -r -a DOCKER_IMAGE <<< "${DOCKER_IMAGE}" # Convert string into array of string

tag=("--tag ${DOCKER_IMAGE[0]}:$version" "--tag ${DOCKER_IMAGE[1]}:$version")
if [[ "$version" != *"-beta."* ]]; then # If the version is a prerelease then don't publish on the short version code
  tag[0]="${tag[0]} --tag ${DOCKER_IMAGE[0]}:${version%%.*} --tag ${DOCKER_IMAGE[0]}:latest" # Remove all after the first dot (dot included)
  tag[1]="${tag[1]} --tag ${DOCKER_IMAGE[1]}:${version%%.*} --tag ${DOCKER_IMAGE[1]}:latest" # Remove all after the first dot (dot included)
fi

# build the docker image
docker buildx build \
  --output type=image,push=true \
  --platform "amd64,arm64,arm" \
  ${tag[0]} \
  --build-arg CI=true \
  --cache-from type=local,src=~/.docker-cache/template-node12 \
  --cache-to type=local,dest=~/.docker-cache/template-node12 \
  template/node12

docker buildx build \
  --output type=image,push=true \
  --platform "amd64,arm64,arm" \
  ${tag[1]} \
  --build-arg CI=true \
  --build-arg TEMPLATE_IMAGE=lenra/template-node12:${version} \
  --cache-from type=local,src=~/.docker-cache/devtools-node12 \
  --cache-to type=local,dest=~/.docker-cache/devtools-node12 \
  --file=Dockerfile.devtools \
  .
