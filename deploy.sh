#!/usr/bin/env bash
mvn clean package
aws s3 cp target/lambda-transcoder-fanout-*-SNAPSHOT-jar-with* s3://sr-lambda/ --profile pete-work