#!/usr/bin/env bash
export VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.3.1:exec)
mvn clean package
aws s3 cp target/lambda-transcoder-fanout-$VERSION-jar-with-dependencies.jar s3://sr-lambda/ --profile pete-work