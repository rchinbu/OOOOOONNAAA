import java.util.*;


public class Player {

	ArrayList<Card> hand = new ArrayList<Card>();

	String name;

	Deck deck;

	public Player(String name, Deck deck) {
		this.name = name;
		this.deck = deck;
		for(int i = 0; i < 7; i++) {
		   hand.add(deck.draw());
		}
	}

	public void getCards() {
		hand.add(deck.draw());
	}

	public void playCard(int cardPosition) {
		deck.discardCard(hand.remove(cardPosition));
	}

}
