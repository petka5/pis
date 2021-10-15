#!/bin/bash -ex

./fly --target petka login  --concourse-url http://localhost:8081 -u petka -p petka
./fly -t petka set-pipeline -p hello-world -c ./hello_world.yaml
