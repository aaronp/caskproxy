#!/usr/bin/env bash


dn=$(dirname $0)

# this file gets mounted/used within the Docker image
source $(dirname $0)/build.sh

pushd $(dirname $0)/src/main/scala

# run
debug

popd