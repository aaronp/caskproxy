#!/usr/bin/env bash

# just for convenience in local development:
# source ./build.sh && clean
function clean() {
  rm *.jar
  rm -rf target
}

function ensureCli() {
  which scala-cli || brew install Virtuslab/scala-cli/scala-cli
}

# used by Dockerfile to create the app.jar fat jar
function fatJar() {
  [[ -f app.jar ]] || ensureCli && scala-cli package App.scala -o app.jar --assembly
}

# just for convenience/documentation in how to go about local development:
# source ./build.sh && debug
function debug() {
  ensureCli && scala-cli App.scala
}

# https://scala-cli.virtuslab.org/docs/commands/setup-ide
function ide() {
  ensureCli && scala-cli setup-ide . --scala 3.1.0
}

# just for convenience in running the instrumented fat-jar locally
# source ./build.sh && run
function run() {
  fatJar
  java -jar app.jar
}
