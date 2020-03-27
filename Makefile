JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		Number.java \
		Color.java \
		Card.java \
		Deck.java 
#DrawPile.java \
#DiscardPile.java \
#Hand.java \
#Player.java \
#GameState.java \
#GamePlay.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
