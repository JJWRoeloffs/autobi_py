# AuToBI-py

A python wrapper and interfacing library around AuToBI. In early stages of development

## AuToBI

The AuToBI system for automatically generating prosody transcriptions, released under the Apache2 license. Only slightly modified for this version. There are two major changes to the original: the migration from Apache Ant to sbt, which is used for better cross-compiling, and the migration from java 6 to java 20, which was needed for loading the code into python. Despite all these changes, the functional behavior should be identical to the original.

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

## Adapter

The AuTOBI adapter is a minimal Scala library written to turn the AuToBI application into a library. It exposes core functionality of the AuToBI system in a way that makes it possible to interface it in a sensible manner. Theoretically speaking, it could be interfaced in any JVM language, however, it was written with py4j in mind, and thus most major functions only take simple collections as their inputs, making them less effective than one could expect of a native library. Usually, this package will only be build as a dependency of the python library.

build with sbt:

```sh
# Compile only the adapter
sbt "adapter/compile"

# Compile the adapter into an "Uber-jar" that contains all dependencies.
# It is this exact jar that is being interfaced in the python library
sbt "adapter/assembly"
```

run tests:

```sh
# Currently, because of a lack of time, the test suite is only very minimal
sbt "adapter/test"
```

## Python library

The main python library is the part of this application that is actually being used. This is the ultimate end-point of this project. It exposes the AuToBI adapter in a python interface using py4j, and contains some needed boilerplate to be able to call this Java/Scala code like it is native python.

To run any Java or Scala code, a JVM needs to be running with the interfaced code available (through Java Reflections) in the jar. To compile this jar, move it to a location within the python sources and write the needed python file to tell the other python sources where it is, run:

```sh
sbt "python/assembly"
```

To work with the actual code, first `cd python`. After this, you are in a regular python library project. To run the tests run:

```sh
# Install current version of package locally to be able to test it.
python3 -m pip install -e .

python3 -m pytest --cov=autobi tests/
```
