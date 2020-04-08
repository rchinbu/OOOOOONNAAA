import java.util.*;


public class Player {

	ArrayList<Card> hand = new ArrayList<Card>();

	String name;

	public Player(String name, ArrayList<Card> hand) {
		this.name = name;
		this.hand = hand;
	}

	public void getCards(Card card) {
		hand.add(card);
	}

	public Card playCard() {
		return hand.remove(cardPosition);
	}

}
