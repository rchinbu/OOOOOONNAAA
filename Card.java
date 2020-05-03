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

        public boolean isValidPlay(Color color, Number number) {
                if (this.color == color) {
                        return true;
                } else if (this.color == color.WILD) {
                        return true;
                } else if (this.number == number) {
                        return true;
                } else {
                        return false;
                }
        }
}
