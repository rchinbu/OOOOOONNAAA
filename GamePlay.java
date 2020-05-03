import java.util.*;
import java.io.*;
import java.util.Random;

//creates a game on a single computer with four single-user controlled players
//currently only works on the command line, GUI not yet implemented

public class GamePlay {
        public static void main(String args[]) {
                Boolean hasWinner=false;
                String winner="none";

                //initialize deck, draw pile, and discard pile
                //deck becomes irrelevant and empty, should we leave this to garbage collector?
                Deck gamePiles = new Deck();

                //initialize the four players
                //Riley changed player

                ArrayList<Player> players = new ArrayList<>();

                players.add(new Player("Maggie"));
                players.add(new Player("Julia"));
                players.add(new Player("Riley"));
                players.add(new Player("Tara"));

                //Pass 7 cards to each person
                for (int i = 0; i < 7; i++) {
                        for (Player player : players) {
                                player.giveCard(gamePiles.draw());
                        }
                }

                //after cards are dealt out, top draw pile card goes onto discard pile
                gamePiles.discardCard(gamePiles.draw());
                Color firstColor = gamePiles.topDiscardCard().getColor();
                Number firstNumber = gamePiles.topDiscardCard().getNumber();

                Scanner input = new Scanner(System.in);

                if (firstColor == Color.WILD) {
                        firstColor = players.get(1).chooseColor(input);
                }
                if (firstNumber == Number.DRAW2) {
                        for (int i = 0; i < 2; i++) players.get(1).giveCard(gamePiles.draw());
                }
                if (firstNumber == Number.DRAW4) {
                        for (int i = 0; i < 4; i++) players.get(1).giveCard(gamePiles.draw());
                }


                GameState gameState = new GameState(players, firstColor, firstNumber);

                //game
                while (!hasWinner) {

                        //added for the sake of knowing what player we are on in the preliminary stages
                        Player player = gameState.getTurn();
                        System.out.println("\nHi, " + player.name + "!");
                        Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), input);
                        if (playedCard == null) {
                                playedCard = player.playAfterDraw(gamePiles.draw(), gameState.getColor(), gameState.getNumber(), input);
                        }
                        if (playedCard != null) {
                                gamePiles.discardCard(playedCard);
                                if (playedCard.getColor() == Color.WILD) {
                                        player = gameState.update(player.chooseColor(input), playedCard.getNumber());
                                } else {
                                        player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                }
                                if (playedCard.getNumber() == Number.DRAW2) {
                                        for (int i = 0; i < 2; i++) player.giveCard(gamePiles.draw());
                                } else if (playedCard.getNumber() == Number.DRAW4) {
                                        for (int i = 0; i < 4; i++) player.giveCard(gamePiles.draw());
                                }
                        } else {
                                player = gameState.update(null, null);
                        }

                        for (Player player1 : players) {
                                if (player1.hand.isEmpty()) {
                                        hasWinner = true;
                                        winner = player.name;
                                }
                        }
                }

                if (!winner.equals("none")) {
                        System.out.println(winner);
                }
        }

/*
 * //functions relates to Player.java
        //get a random card from deck and put into player's hand
        //public void getcard(Player name, Deck deck)
        {
                Random rand = new Random();
                int cardPosition = rand.nextInt(deck.getSize());
                name.giveCard(deck.remove(cardPosition));
        }
        //discard a choosen card from player's hand to drawpile
        //public void discardCard(Player name, Card card, Deck deck)
        {
                //find the card's position
                int cardPos =0;
                for (int i; i < name.hand.size(); i++){
                        if (name.hand.get(i)== card){
                                cardPos=i;
                                break;
                        }
                }
                //remove this card from player's hand
                name.playCard(cardPos);
                //add this card to the discardPile
                deck.discardCard(card);
        }
        //get the number of card a player has
        public int playHandsize (Player name)
        {
                return name.hand.size();
        }
        // return true if player is out of cards return false otherwise
        public boolean isPlayerEmpty(Player name )
        {
        if (name.hand.isEmpty())
                return true;
        else
                return false;
        }
*/

}
