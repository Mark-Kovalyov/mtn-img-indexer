#!/bin/bash -e

mkdir -p bin/linux
mkdir -p bin/win

mvn package -P default

cp -f target/mtn-img-indexer-1.0-SNAPSHOT.jar ./bin/mtn-img-indexer.jar
cp -f src/main/resources/mtn-img-indexer.sh ./bin
cp -f src/main/resources/mtn-img-indexer.cmd ./bin

cp -f target/mtn-img-indexer ./bin/linux

