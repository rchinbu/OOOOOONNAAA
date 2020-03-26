import java.util.Stack;
import java.util.*;

public class Deck {

	Stack<Character> dDeck = new Stack<>();
	private static Card orderedDeck[] = new Card[108];

	//shuffledDeck	
	public Deck() {
		
		char s = 'S';
		char r = 'R';
		char d = 'D';
		char w = 'W';
		char k = 'K';


		int index = 0;
		char color;
		char value;
		for(int i = 0; i < 4; i++) {
			switch(i) {
				case 0: color = 'R'; break;
				case 1: color = 'B'; break;
				case 2: color = 'G'; break;
				case 3: color = 'Y'; break;
				default: color = 'X'; break;
			}
			orderedDeck[index] = new Card((char)48, color); index++;
			for(int j = 1; j < 10; j++) {
				value = (char)(j+48);
				orderedDeck[index] = new Card(value, color); index++;
				orderedDeck[index] = new Card(value, color); index++;
			}
			orderedDeck[index] = new Card(s,color); index++;
			orderedDeck[index] = new Card(s,color); index++;
			orderedDeck[index] = new Card(r,color); index++;
			orderedDeck[index] = new Card(r,color); index++;
			orderedDeck[index] = new Card(d,color); index++;
			orderedDeck[index] = new Card(d,color); index++;
			orderedDeck[index] = new Card(w,'N'); index++;
			orderedDeck[index] = new Card(k,'N'); index++;
		}
	}

	public static void main(String[] args) {
		Deck deck = new Deck();
		for(int i = 0; i < 108; i++) {
			System.out.println(orderedDeck[i]);
		}
	}

}
