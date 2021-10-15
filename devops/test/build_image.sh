#!/bin/bash -ex

#https://nolife.cyou/how-to-build-and-push-a-docker-image-to-a-private-repository-in-concourse-ci-task/
docker build -t test .
docker tag test:latest localhost:5000/test

docker push localhost:5000/test

