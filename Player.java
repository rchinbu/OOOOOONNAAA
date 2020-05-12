import java.util.*;
import java.io.Serializable;

public interface Player extends Serializable {

        //ArrayList<Card> hand;
        //String name;

        //public Player(String name);

        public void giveCard(Card card);

        public void removeCard(Card card);

        public Card playCard(Color lastColor, Number lastNumber, Scanner input);

        public Color chooseColor(Scanner input);

        public Card playAfterDraw(Card newCard, Color lastColor, Number lastNumber, Scanner input);
}
