#!/bin/bash -e

mkdir -p bin
mvn package -P default

cp -f target/mtn-img-indexer-1.0-SNAPSHOT.jar ./bin/mtn-img-indexer.jar
cp -f mtn-img-indexer.sh ./bin

