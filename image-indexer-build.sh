#!/bin/bash -e

mkdir -p bin
mvn package -P ImageIndexer

cp -f target/mtn-img-indexer.jar ./bin/mtn-img-indexer.jar
cp -f src/main/resources/mtn-img-indexer.sh ./bin
cp -f src/main/resources/mtn-img-indexer.cmd ./bin

