#!/usr/bin/env bash

JVM_OPTS="${JVM_OPTS:-} -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

exec java ${JVM_OPTS} -jar /lib/server-*.jar
