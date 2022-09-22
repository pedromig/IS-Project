#!/usr/bin/sh

# Settings
SRC_DIR=.
DST_DIR="../src/main/java"

# Protoc compiler binary path
PROTOC_PATH="../../../tools/protoc-linux-x86_64/bin"

"${PROTOC_PATH}/protoc" -I${SRC_DIR} --java_out=${DST_DIR} ${SRC_DIR}/model.proto

