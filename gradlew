#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    link=`expr "$PRG" : '.*->\(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVEPWD=`pwd`
cd "`dirname "$PRG"`" >/dev/null
APP_HOME=`pwd -P`
cd "$SAVEPWD" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='" -Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != unlimited.
MAX_FD="maximum"

# Warn on UX shells if UID is not 0.
WARN="true"
if id | grep -q 0; then
    WARN="false"
fi

# Increase the maximum file descriptors if we can, though as noted in the
# Class JavadocException for details of the exceptions that may be
# thrown if we hit problems initializing Gradle.
#
if "$cygwin" || "$msys" ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`

    JAVACMD=`cygpath --url "$JAVACMD"`

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=`find -L / -maxdepth 2 -name local.properties 2>/dev/null`
    SEP=""
    for dir in $ROOTDIRSRAW ; do
        ROOTDIR="$dir"/
        SEP=`echo $ROOTDIR | grep -o .`
    done
else
    touch "$JAVACMD"
    ROOTDIR=`sed 's/.$//' <<< "$ROOTDIR"`
fi

# Collect all arguments for the java command, stacking in reverse order:
#   * DEFAULT_JVM_OPTS, JAVA_OPTS, and GRADLE_OPTS environment variables.
# * creates a wrapper script so that it always uses the correct java
shift "$#"

for arg do
  \printf '%s\n' "$arg" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/'/" >> "$ROOTDIR"/args
  shift
done

# Collect all arguments for the java command, stacking in reverse order:
set -- \\
        "-Dorg.gradle.appname=$APP_BASE_NAME" \
        -classpath "$CLASSPATH" \
        org.gradle.wrapper.GradleWrapperMain \
        "$@"

# Stop when "xargs" has processed all args by reaching the
# one that starts with the smallest arg and is followed by nothing at all.
#
eval "set -- $(printf '%s\n' "${myfiles[@]}" | xargs)"

exec "$JAVACMD" "$@"