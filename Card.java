import java.util.*;

public class Card {

	char cardValue;
	char cardColor;

	Card(char cardValue, char cardColor) {
		this.cardValue = cardValue; 
		//0 1 2 3 4 5 6 7 8 9 
		//S(skip) R(reverse) D(draw 2) W(wild) K(draw 4)
		this.cardColor = cardColor;
		//R(red) G(green) B(blue) Y(yellow) N(none)
	}

	public String toString() {
		String color;
		String value;
		switch(cardColor) {
			case 'R': color = "Red "; break;
			case 'G': color = "Green "; break;
			case 'B': color = "Blue "; break;
			case 'Y': color = "Yellow "; break;
			case 'N': color = ""; break;
			default: color = "error "; break;
		}
		if(Character.isDigit(cardValue)) {
			value = Character.toString(cardValue);
		} else {
			switch(cardValue) {
				case 'S': value = "Skip"; break;
				case 'R': value = "Reverse"; break;
				case 'D': value = "Draw two"; break;
				case 'W': value = "Wild card"; break;
				case 'K': value = "Pick up four"; break;
				default: value = "error"; break;
			}
		}
		return color + value;
	}

	public char getCardValue(Card c) {
		return c.cardValue;
	}
	public char getCardColor(Card c) {
		return c.cardColor;
	}

	public boolean canPlay(Card currentCard, Card previousCard) {
		if((getCardValue(currentCard) == getCardValue(previousCard)) ||
				(getCardColor(currentCard) == getCardColor(previousCard)) ||
				(getCardColor(currentCard) == 'N')) {
					return true;
				} else {
					return false;
				}
	}

	
}
