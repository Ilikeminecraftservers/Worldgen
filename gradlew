#!/usr/bin/env sh
##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Remove Gradle's own JDK detection if necessary
# Add any environment variable overrides here

# Launch Gradle
exec java -jar gradle/wrapper/gradle-wrapper.jar "$@"
