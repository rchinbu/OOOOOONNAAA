import java.util.*;

public class Deck {

        private static Queue<Card> drawPile = new LinkedList<>();
        private static Stack<Card> discardPile = new Stack<>();
        private static ArrayList<Card> deck = new ArrayList<>();

        //Constructor
        public Deck() {
                for (Color color : Color.normal_colors()) {
                        for (Number number : Number.normal_numbers()) {
                                if(number == Number.normal_numbers()[0]) {
                                        this.deck.add(new Card(color, number));
                                } else {
                                        this.deck.add(new Card(color, number));
                                        this.deck.add(new Card(color, number));
                                }
                        }
                        for (Number number : Number.special_nonwild()) {
                                this.deck.add(new Card(color, number));
                                this.deck.add(new Card(color, number));
                        }

                        this.deck.add(new Card(Color.WILD, Number.NONE));
                        this.deck.add(new Card(Color.WILD, Number.DRAW4));
                }
        Collections.shuffle(this.deck);
        while(!this.deck.isEmpty()) {
                drawPile.add(deck.remove(0));
                }
        }

        //DrawPile methods
        private void shuffle() {
                ArrayList<Card> copyDrawPile = new ArrayList<>();
                while(drawPile.peek != null) {
                        copyDrawPile.add(drawPile.remove());
                }
                Collections.shuffle(copyDrawPile);
                while(!copyDrawPile.isEmpty()) {
                        drawPile.add(copyDrawPile.remove(0));
                }
        }

        public Card draw() {
                if (drawPile.peek()==null) {
                        this.getNewDrawPile();
                }
                return drawPile.remove();
        }

        //DiscardPile methods
        private void getNewDrawPile() {
                Card top = discardPile.pop();
                while (!discardPile.isEmpty()) {
                        drawPile.add(discardPile.pop());
                }
                discardPile.push(top);
                this.shuffle;
        }
        public void discardCard(Card card) {
                discardPile.push(card);
        }

        public Card topDiscardCard() {
                return discardPile.peek();
        }
}
