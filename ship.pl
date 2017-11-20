#!/usr/bin/perl


javac -sourcepath src -cp "lib/*" -d bin @sources.txt
cd bin
jar cvf $filename *
mv $filename $destination
