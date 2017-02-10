# modify the paths appropriately
JDKPATH = /usr/java/jdk1.6.0_20
LIBPATH = .:..
CLASSPATH = $(LIBPATH)
BINPATH = $(JDKPATH)/bin
JAVAC = $(JDKPATH)/bin/javac -classpath $(CLASSPATH)
JAVA  = $(JDKPATH)/bin/java  -classpath $(CLASSPATH)

# run project

RUNSPATIAL:
	$(JAVAC) bo/*.java
	$(JAVAC) data/*.java
	$(JAVAC) index/rtree/*.java
	$(JAVAC) test/*.java
	$(JAVAC) util/*.java
	
runSpatial:
	cd test;
	$(JAVA) spatial.test.Prompt
	