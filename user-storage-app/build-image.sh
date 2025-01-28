#!/usr/bin/env bash

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
TAG="rmegyesi/rmbk-user-storage-app:$VERSION"

docker build -t $TAG .
