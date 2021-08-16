#!/bin/bash -e

mkdir -p bin
mvn package -P YUVDecomposer

cp -f target/mtn-img-indexer-1.0-SNAPSHOT.jar ./bin/mtn-img-indexer.jar


