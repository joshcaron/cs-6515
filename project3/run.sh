#! /bin/sh

javac src/Edge.java src/Graph.java src/GraphExample.java src/GraphReader.java src/GraphServer.java src/Node.java src/NodeExample.java src/Path.java src/PathExample.java -d bin
cd bin
java GraphServer
