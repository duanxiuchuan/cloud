#!/usr/bin/env bash
mvn package

docker build -t rd-admin .