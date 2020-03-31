import java.util.*;

public class Deck {

	static ArrayList<Card> deck = new ArrayList<>();
	static {
		for (Color color : Color.normal_colors()) {
			for (Number number : Number.normal_numbers()) {
				if(number == Number.normal_numbers()[0]) {
					deck.add(new Card(color, number));
				} else {
					deck.add(new Card(color, number));
					deck.add(new Card(color, number));
				}
			}
			for (Number number : Number.special_nonwild()) {
				deck.add(new Card(color, number));
				deck.add(new Card(color, number));
			}

			deck.add(new Card(Color.WILD, Number.NONE));
			deck.add(new Card(Color.WILD, Number.DRAW4));
		}
	}
	
	static Queue<Card> drawPile = new LinkedList<>();
	static Queue<Card> discardPile = new LinkedList<>();
	

	//Constructor
	public Deck() {
		Random rand = new Random();
		while(! deck.isEmpty()) {
			drawPile.add(deck.get(rand.nextInt(deck.size())));
		}
	}

	//DrawPile methods
	
	Random randomList = new Random();
	private void shuffle() {
		ArrayList<Card> copyDrawPile = new ArrayList<>();
		while(drawPile.peek() != null) {
			copyDrawPile.add(drawPile.remove());
		}
		while(! copyDrawPile.isEmpty()) {
			drawPile.add(copyDrawPile.remove(randomList.nextInt(copyDrawPile.size())));
		}
	}
		 
	
	public Card draw() {
		if(drawPile.peek() == null) {
			this.getNewDrawPile();
		}
		return drawPile.remove();
	}

	//DiscardPile Methods
	
	private void getNewDrawPile() {						
		(for int i=0; i<discardPile.size(); i++) {
			drawPile.add(discardPile.remove());
		}
		this.shuffle();
	}

	public void discardCard(Card) {
		discardPile.add(Card);
	}
	
	//peek at top card method
}

