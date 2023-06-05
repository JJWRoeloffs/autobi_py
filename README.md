# AuToBI-py

A python wrapper and interfacing library around AuToBI. In early stages of development


## AuToBI
Copyright (c) 2009-2017 Andrew Rosenberg, originally released under the Apache2 licence. Only slightly modified for this version, with some very small bugfixes. Most major change is the migration from Apache Ant to sbt, which is used for better cross-compiling

Build with sbt:
```sh
# Compile only autobi itself
sbt "autobi/compile"

# Create one "Uber-jar" that contains all dependencies
sbt "autobi/assembly"
```

Run tests:
```sh
sbt "autobi/test"
```