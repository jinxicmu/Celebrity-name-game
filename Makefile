JFLAGS = -cp
JAR = ./lib/junit.jar:./lib/mockito-all-1.9.5.jar
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(JAR) LongestNameChain.java LongestNameChainFunctionalTest.java LongestNameChainTest.java

CLASSES = \
	LongestNameChain.java \
	LongestNameChainTest.java \
	LongestNameChainFunctionalTest.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class *~

