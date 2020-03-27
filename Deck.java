import java.util.*;

public class Deck {

	public static ArrayList<Card> deck = new ArrayList<>();

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
	
	public static ArrayList<Card> 
	

	public static void main(String[] args) {
		for(int i = 0; i < deck.size(); i++) {
			System.out.println(deck.get(i));
		}
	}

}


/* Problems:
 * I don't know if you can even do the for (variable in array) {} iterator 
 * This currently adds two 0s for each color, which, of course, is one extra per color. */
