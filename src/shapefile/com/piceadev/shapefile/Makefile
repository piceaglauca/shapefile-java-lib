CLASSPATH = /home/scott/piceadev/shapefile-java-lib/src/shapefile/:/usr/share/java/commons-io.jar:
JFLAGS = -g -cp $(CLASSPATH) -Xlint:deprecation
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(CLASSES)

CLASSES = \
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

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class internal/*.class

test:
	$(JVM) -cp $(CLASSPATH) com.piceadev.shapefile.EsriTester /home/scott/piceadev/shapefile-java-lib/src/shapefile/com/piceadev/shapefile/testing/testdata/test-point.shp
	$(JVM) -cp $(CLASSPATH) com.piceadev.shapefile.EsriTester /home/scott/piceadev/shapefile-java-lib/src/shapefile/com/piceadev/shapefile/testing/testdata/test-polyline.shp
