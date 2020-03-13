#!/usr/bin/env bash

set -ex

SCRIPT_DIR="${BASH_SOURCE%/*}"
TARGET_DIR="$SCRIPT_DIR/../target"
PLUGIN_JAR_FILE="$TARGET_DIR/presto-query-logger.jar"
DEPENDENCY_DIR="$TARGET_DIR/dependency"
PLUGIN_CONFIG_DIR="$SCRIPT_DIR/../sample_config"

if [[ ! -f "$PLUGIN_JAR_FILE" || ! -d "$DEPENDENCY_DIR" ]]; then
  exit 1
fi

WORK_DIR="$SCRIPT_DIR/tmp_work"
mkdir -p "$WORK_DIR/target"
mkdir -p "$WORK_DIR/plugin_config"

cp "$PLUGIN_JAR_FILE" "$WORK_DIR/target/"
cp "$DEPENDENCY_DIR"/* "$WORK_DIR/target/"
cp "$PLUGIN_CONFIG_DIR"/* "$WORK_DIR/plugin_config"

docker build "$SCRIPT_DIR" -t "presto-with-plugin"

rm -fr "$WORK_DIR"
