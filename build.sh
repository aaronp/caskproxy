#!/usr/bin/env bash


export SRC_FILES="."

# just for convenience in local development:
# source ./build.sh && clean
function clean() {
  rm *.jar
  rm -rf target
}

function ensureCli() {
  (which scala-cli > /dev/null 2>&1) || brew install Virtuslab/scala-cli/scala-cli
}

# used by Dockerfile to create the app.jar fat jar
function fatJar() {
  [[ -f app.jar ]] || ensureCli && scala-cli package "$SRC_FILES" -o app.jar --assembly
}

# just for convenience/documentation in how to go about local development:
# source ./build.sh && debug
function debug() {
  ensureCli && scala-cli "$SRC_FILES" --main-class ProxyServer
}

# https://scala-cli.virtuslab.org/docs/commands/setup-ide
function ide() {
  ensureCli && scala-cli setup-ide . --scala 3.2.0
}

# just for convenience in running the instrumented fat-jar locally
# source ./build.sh && run
function run() {
  fatJar
  java -jar app.jar
}
