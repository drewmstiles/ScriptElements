#!/bin/bash

javac -sourcepath src -cp "lib/*" -d bin @sources.txt
cd bin
jar cvf script-elements.jar *
mv script-elements.jar $1
