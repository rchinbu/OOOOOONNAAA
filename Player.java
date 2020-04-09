import java.util.*;


public class Player {

	ArrayList<Card> hand = new ArrayList<Card>();

	String name;

	public Player(String name, ArrayList<Card> hand) {
		this.name = name;
		this.hand = hand;
		
		//added this back for the sake of the first version
		for(int i = 0; i < 7; i++) {
                        this.getCards();
                }
		//putting comments around it to know we have to work on it
		//I think this for loop should be in GamePlay.java as its related to deck
	}

	public void getCards(Card card) {
		hand.add(card);
	}

	public Card playCard( int cardPosition) {
		return hand.remove(cardPosition);
	}

}
