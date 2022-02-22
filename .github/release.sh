#!/bin/bash

set -xe # Show output on the logs

function get_tag {
  version="$1" # Get version tag
  DOCKER_IMAGE="$2"
  
  regex='([0-9]+.[0-9]+.[0-9]+)(-([a-z]+).([0-9]+))?'
  
  if [[ $version =~ $regex ]]; then
    v="${BASH_REMATCH[1]}"
    channel="${BASH_REMATCH[3]}"
    channel_version="${BASH_REMATCH[4]}"

    tag="--tag ${DOCKER_IMAGE}:${version#v}"

    regex='([0-9]+).([0-9]+).([0-9]+)'
    if [[ $v =~ $regex ]]; then
      major=${BASH_REMATCH[1]}
      minor=${BASH_REMATCH[2]}
      patch=${BASH_REMATCH[3]}

      arr_version=( "${major}" "${major}.${minor}" "${major}.${minor}.${patch}" )
      if [[ -n "${channel}" ]]; then
        tag="${tag} --tag ${DOCKER_IMAGE}:${channel}"
        for i in "${arr_version[@]}"; do
          tag="${tag} --tag ${DOCKER_IMAGE}:${i}-${channel}"
        done
      else
        tag="--tag ${DOCKER_IMAGE}:latest"
        for i in "${arr_version[@]}"; do
          tag="${tag} --tag ${DOCKER_IMAGE}:${i}"
        done
      fi
      return 0
    else
      echo "Version '$v' didn't pass Regex '$regex'." 1>&2
      return 1
    fi
  else
    echo "Version '$version' didn't pass Regex '$regex'." 1>&2
    return 1
  fi
}

VERSION="$1" # Get version tag

read -r -a docker_images <<< "${DOCKER_IMAGE}" # Convert string into array of string

get_tag "$VERSION" "${docker_images[0]}"
exit_code=$?
if [[ "$exit_code" != "0" ]]; then
  exit $exit_code
fi

docker buildx build \
  --output type=image,push=true \
  --platform "linux/amd64,linux/arm64,linux/arm" \
  ${tag} \
  --build-arg CI=true \
  template/node12

get_tag "$VERSION" "${docker_images[1]}"
exit_code=$?
if [[ "$exit_code" != "0" ]]; then
  exit $exit_code
fi

docker buildx build \
  --output type=image,push=true \
  --platform "amd64,arm64,arm" \
  ${tag} \
  --build-arg CI=true \
  --build-arg TEMPLATE_IMAGE=lenra/template-node12:${version} \
  --file=Dockerfile.devtools \
  .
