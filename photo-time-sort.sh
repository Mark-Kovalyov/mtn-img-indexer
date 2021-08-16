#!/bin/bash -e

mkdir -p bin
mvn package -P PhotoTimeSort

cp -f target/photo-time-sort.jar ./bin/photo-time-sort.jar


