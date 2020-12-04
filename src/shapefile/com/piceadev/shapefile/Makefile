CLASSPATH = .:/usr/share/java/commons-io.jar:
JFLAGS = -g -cp $(CLASSPATH)
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	ShapefileReader.java \
	internal/*.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class internal/*.class
