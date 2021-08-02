#!/bin/bash -e

mkdir -p bin
mvn package -P default
mvn install dependency:copy-dependencies -P default
cp -f target/image-indexer.jar ./bin
rsync target/dependency/* ./bin -r