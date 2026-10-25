#!/usr/bin/env sh
set -eu
MAVEN_VERSION="3.9.11"
MAVEN_ROOT="${HOME}/.maven"
MAVEN_HOME_LOCAL="${MAVEN_ROOT}/apache-maven-${MAVEN_VERSION}"
if [ ! -x "${MAVEN_HOME_LOCAL}/bin/mvn" ]; then
  echo "Maven ${MAVEN_VERSION} não encontrado. Este script baixa a distribuição oficial."
  mkdir -p "${MAVEN_ROOT}"
  ZIP="${TMPDIR:-/tmp}/apache-maven-${MAVEN_VERSION}-bin.zip"
  URL="https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.zip"
  if command -v curl >/dev/null 2>&1; then curl -fL "$URL" -o "$ZIP"
  elif command -v wget >/dev/null 2>&1; then wget -O "$ZIP" "$URL"
  else echo "Instale curl ou wget e tente novamente."; exit 1; fi
  unzip -oq "$ZIP" -d "$MAVEN_ROOT"
  rm -f "$ZIP"
fi
if [ "$#" -eq 0 ]; then set -- clean package; fi
exec "${MAVEN_HOME_LOCAL}/bin/mvn" "$@"
