import java.util.*;


public class AIPlayer {

	ArrayList<Card> hand;
	String name;

	public Player(String name) {
		this.name = name;
		this.hand = new ArrayList<Card>();
		
		//added this back for the sake of the first version
		//for(int i = 0; i < 7; i++) {
                //        this.getCards();
                //}
		//putting comments around it to know we have to work on it
		//I think this for loop should be in GamePlay.java as its related to deck
	}

	public void giveCard(Card card) {
		hand.add(card);
	}
  Random rand = new Random();
	public Card playCard() {
		return hand.remove(rand.nextInt(hand.size());
	}

}
