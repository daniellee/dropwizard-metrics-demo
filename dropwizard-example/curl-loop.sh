#!/bin/sh
set -eux

curl http://localhost:8082/hello-world?[1-200]
