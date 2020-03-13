#!/usr/bin/env bash

set -x

SCRIPT_DIR="${BASH_SOURCE%/*}"
QUERIES_DIR="$SCRIPT_DIR/queries"

for query_file in "$QUERIES_DIR"/*.sql; do
  echo "[Run: $query_file]"
  presto --server localhost:8080 --catalog jmx --schema current -f "$query_file"
done
