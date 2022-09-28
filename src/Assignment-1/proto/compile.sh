#!/usr/bin/sh

# Settings
SRC_DIR=.
DST_DIR="../src/main/java"

# Protoc compiler binary path
PROTOC_PATH="../../../tools/"
case $(uname | tr '[:upper:]' '[:lower:]') in
  linux* | darwin*) PROTOC_PATH="${PROTOC_PATH}protoc-linux-x86_64/bin";;
  *) PROTOC_PATH="${PROTOC_PATH}protoc-win-x86_64/bin" ;;
esac

"${PROTOC_PATH}/protoc" -I${SRC_DIR} --java_out=${DST_DIR} ${SRC_DIR}/model.proto

