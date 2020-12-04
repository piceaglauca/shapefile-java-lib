CLASSPATH = .:/usr/share/java/commons-io.jar:
JFLAGS = -g -cp $(CLASSPATH)
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) internal/*.java $*.java

CLASSES = \
	ShapefileReader.java \
	internal/EsriFeatureFactory.java \
	internal/EsriFeature.java \
	internal/EsriPoint.java \
	internal/EsriPolyline.java \
	internal/LittleEndianInputStream.java \
	internal/Shapefile.java \
	internal/ShapefileConstants.java \
	internal/ShpInputStream.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class internal/*.class
