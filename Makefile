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
<<<<<<< HEAD
#DrawPile.java \
#DiscardPile.java \
#Hand.java \
#Player.java\
#GameState.java \
=======
		Player.java \
		GameState.java \
>>>>>>> 77b2728743e96fdda4611f672f6335df05e5b178
#GamePlay.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
