public class Card {
	Number number;
	Color color;
	
	public Card(Color color, Number number) {
		this.color = color;
		this.number = number;
	}

	public Color getColor() {
		return this.color;
	}

	public Number getNumber() {
		return this.number;
	}

	public String toString() {
		return this.color.toString() + " " + this.number.toString();
	}

	public boolean isValidPlay(Card card) {
		if (this.color == card.getColor()) {
			return true;
		} else if (this.color == color.WILD) {
			return true;
		} else if (this.number == card.getNumber()) {
			return true;
		} else {
			return false;
		}
	}
}
