TOPDIR = $(shell pwd)
CLASSDIR = $(TOPDIR)/classes
SOURCEDIR = $(TOPDIR)/src
TESTDATADIR = $(SOURCEDIR)/com/piceadev/shapefile/testing/testdata
CLASSPATH = $(CLASSDIR):/usr/share/java/commons-io.jar:
JFLAGS = -g -cp $(CLASSPATH) -Xlint:deprecation
JC = javac
JVM = java
JAR = jar
JARFILE = $(TOPDIR)/shp-manager.jar
.SUFFIXES: .java .class
#.java.class:
	#$(JC) $(JFLAGS) $(JAVA_SOURCES)

JAVA_SOURCES = $(shell find $(SOURCEDIR) -type f -name "*.java" -print)
JAVA_OUTPUTS = $(patsubst %.java,%.class,$(JAVA_SOURCES))
#CLASSES = \
	EsriFileManager.java \
	EsriTester.java \
	internal/EsriConstants.java \
	internal/EsriFeature.java \
	internal/EsriFeatureFactory.java \
	internal/EsriFile.java \
	internal/EsriFileHandler.java \
	internal/EsriIndex.java \
	internal/EsriPoint.java \
	internal/EsriPolyline.java \
	internal/LittleEndianRandomAccessFile.java \
	internal/Shapefile.java \
	internal/ShpFileHandler.java \
	internal/ShxFile.java \
	internal/ShxFileHandler.java

default: 
	$(MAKE) build
	$(MAKE) jar

jar:
	cd $(CLASSDIR); $(JAR) cf $(JARFILE) .

build: 
	$(JC) $(JFLAGS) -d $(CLASSDIR) $(JAVA_SOURCES)

#classes: $(CLASSES:.java=.class)

clean:
	#$(RM) *.class internal/*.class
	$(RM) $(JAVA_OUTPUTS)

test:
	$(JVM) -cp $(CLASSPATH) com.piceadev.shapefile.EsriTester $(TESTDATADIR)/test-point.shp
	$(JVM) -cp $(CLASSPATH) com.piceadev.shapefile.EsriTester $(TESTDATADIR)/test-polyline.shp
