#!/bin/bash -e

mkdir -p bin
mvn package -P PhotoTimeSort
cp -f src/main/resources/photo-time-sort.sh ./bin
cp -f target/photo-time-sort.jar ./bin/photo-time-sort.jar


