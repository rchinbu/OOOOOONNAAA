import java.util.*

public class Deck {
	ArrayList<Card> deck;

	public Deck() {
		for (color in Color.normal_colors()) {
			for (number in Number.normal_numbers()) {
				deck.add(new Card(color, number));
				deck.add(new Card(color, number));
			}
			for (number in Number.special_nonwild()) {
				deck.add(new Card(color, number));
				deck.add(new Card(color, number));
			}

			deck.add(new Card(Color.WILD, Number.NONE));
			deck.add(new Card(Color.WILD, Number.DRAW4));
		}
	}
}


/* Problems:
 * I don't know if you can even do the for (variable in array) {} iterator 
 * This currently adds two 0s for each color, which, of course, is one extra per color. 
