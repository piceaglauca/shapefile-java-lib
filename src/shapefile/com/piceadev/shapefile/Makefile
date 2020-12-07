CLASSPATH = /home/scott/piceadev/shapefile-java-lib/src/shapefile/:/usr/share/java/commons-io.jar:
JFLAGS = -g -cp $(CLASSPATH) -Xlint:deprecation
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) internal/*.java $*.java

CLASSES = \
	ShapefileReader.java \
	internal/EsriFeatureFactory.java \
	internal/EsriFeature.java \
	internal/EsriPoint.java \
	internal/EsriPolyline.java \
	internal/EsriConstants.java \
	internal/LittleEndianInputStream.java \
	internal/Shapefile.java \
	internal/ShpInputStream.java \
	internal/DbfInputStream.java \
	internal/DbfTableModel.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class internal/*.class

test:
	$(JVM) -cp $(CLASSPATH) com.piceadev.shapefile.ShapefileReader /home/scott/piceadev/shapefile-java-lib/src/shapefile/com/piceadev/shapefile/testing/testdata/test-point.shp
	$(JVM) -cp $(CLASSPATH) com.piceadev.shapefile.ShapefileReader /home/scott/piceadev/shapefile-java-lib/src/shapefile/com/piceadev/shapefile/testing/testdata/test-polyline.shp
