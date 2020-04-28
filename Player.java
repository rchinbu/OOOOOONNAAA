import java.util.*;


public class Player {

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

        public Card playCard(Color lastColor, Number lastNumber, Scanner input) {
                ArrayList<Card> available = new ArrayList<>();
                for (Card card : hand) {
                        if (card.isValidPlay(lastColor, lastNumber)) {
                                available.add(card);
                        }
                }
                System.out.println("Here is the card to play to: " + lastColor + " " + lastNumber + "\n");
                if (!available.isEmpty()) {
                        System.out.println("Here are your cards");
                        for (Card card : hand) {
                                System.out.println(card.toString());
                        }
                        System.out.println("\nWhich card would you like to play? You can also choose DRAW instead.");
                        while (true) {
                                String in = input.nextLine();
                                String[] split = in.split("\\s+");
                                if (split.length == 2) {
                                        String col = split[0];
                                        String num = split[1];
                                        for (Card card : available) {
                                                if (
                                                        col.equals(card.getColor().toString()) &&
                                                        num.equals(card.getNumber().toString())
                                                ) return card;
                                        }
                                } else if (in.equals("DRAW")) {
                                        return null;
                                }
                                System.out.println("Invalid input, try again.");
                        }

                } else {
                        System.out.println("You have no valid cards!");
                        return null;
                }
        }

        public Color chooseColor(Scanner input) {
                boolean validInput = false;
                System.out.println("Wild card! Which color would you like to choose? Your options are RED, GREEN, YELLOW, and BLUE");
                while (true) {
                        String chosenColor = input.nextLine();
                        if (chosenColor.equals("RED")) {
                                return Color.RED;
                        } else if (chosenColor.equals("BLUE")) {
                                return Color.BLUE;
                        } else if (chosenColor.equals("GREEN")) {
                                return Color.GREEN;
                        } else if (chosenColor.equals("YELLOW")) {
                                return Color.YELLOW;
                        } else {
                                System.out.println("Invalid option! Try again.");
                        }
                }

        }

        public Card playAfterDraw(Card newCard, Color lastColor, Number lastNumber, Scanner input) {
                System.out.println("Your new card is " + newCard.toString());
                if (newCard.isValidPlay(lastColor, lastNumber)) {
                        while (true) {
                                System.out.println("Would you like to play it? Y/N");
                                String response = input.nextLine();
                                if (response.equals("Y")) return newCard;
                                else if (response.equals("N")) return null;
                                else {
                                        System.out.println("Invalid input! Try again.");
                                }
                        }
                } else {
                        return null;
                }
        }

}
