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

		//TODO: if the first card is a special card, there are some things that need to happen (listed in the rules)
                GameState gameState = new GameState(players, gamePiles.topDiscardCard().getColor(), gamePiles.topDiscardCard().getNumber());


                //game
                while (!hasWinner) {

                        //added for the sake of knowing what player we are on in the preliminary stages
                        System.out.println("\nHi, " + gameState.getTurn().name + "!");
                        ArrayList<Card> available = new ArrayList<>();
                        for (Card card : gameState.getTurn().hand) {
                                if (card.isValidPlay(gamePiles.topDiscardCard().getColor(), gamePiles.topDiscardCard().getNumber())) {
                                        available.add(card);
                                }
                        }

                        //card that player has to match
			//CONCERN: we need to let players know what the color is, since that's specified by the user.
                        System.out.println("Here is the card to play to: " + gamePiles.topDiscardCard().toString() + "\n");

                        //Player has playable cards
                        if (!available.isEmpty()) {

                                System.out.println("Here are your available cards to play:");

                                for (Card card : available) {
                                        System.out.println(card.toString());
                                }

                                //options for turn
                                System.out.println("\nWhich card would you like to play? You can also choose to draw instead.");
                                Scanner input = new Scanner(System.in);
                                String in = input.nextLine();
                                //input.close();
                                String[] split = in.split("\\s+");

                                //if player types in playable card. need to do error checking
                                if (split.length==2) {
                                        String col = split[0];
                                        String num = split[1];
                                        for (Card card : available) {
                                                if (col.equals(card.getColor().toString()) && num.equals(card.getNumber().toString())) {
                                                        Card temp = new Card(card.getColor(), card.getNumber());
                                                        gamePiles.discardCard(temp);
                                                        gameState.getTurn().hand.remove(card);
                                                        break;
                                                }
                                        }
                              //if player types in DRAW. need to implement error checking
                                } else {
                                        Card temp = gamePiles.draw();

                                        //drawn card is a valid play
                                        if (temp.isValidPlay(gamePiles.topDiscardCard().getColor(), gamePiles.topDiscardCard().getNumber())) {
                                                System.out.println("\nHere is your draw!:\n" + temp.toString() + "\n\nWould you like to play it?");
                                                in = input.nextLine();

                                                //player wants to play
                                                        if (in.equals("yes")) {
                                                        //don't need to bother putting it in their hand
                                                                gamePiles.discardCard(temp);

                                                //player does not want to put it down
                                                        } else if (in.equals("no")) {
                                                                gameState.getTurn().giveCard(temp);
                                                        }
                                        //drawn card is not a valid play
                                        } else {

                                                System.out.println("\nHere is your draw!: \n" + temp.toString() + "\nThis card can't be played right now, we'll add it to your hand for now.\n");
                                                gameState.getTurn().giveCard(temp);
                                        }
                                }
                        //if player has no viable cards to play
                        } else {

                                System.out.println("You have no viable cards to play, here's your draw!:");
                                Card temp = gamePiles.draw();
                                System.out.println(temp.toString() + "\n");

                                //draw is valid 
                                if (temp.isValidPlay(gamePiles.topDiscardCard().getColor(), gamePiles.topDiscardCard().getNumber())) {
                                        System.out.println("Do you want to play it?");
                                        Scanner input = new Scanner(System.in);
                                        String in = input.nextLine();

                                        //player decides to play card
                                        if (in.equals("yes")) {
                                                gamePiles.discardCard(temp);
                                        //player decided not to play card
                                        } else if (in.equals("no")) {
                                                gameState.getTurn().giveCard(temp);
                                        }
                                //draw is invalid
                                } else {
                                        System.out.println("Unfortunately, this card can't be played, we'll add it to your hand for now.\n");
                                                gameState.getTurn().giveCard(temp);
                                        }

                        }

                        gameState.update(gamePiles.topDiscardCard().getColor(), gamePiles.topDiscardCard().getNumber());

                        //will keep going until one of the players has no more cards. we need to implement the UNO yell
                        for (Player player : players) {
                                if (player.hand.isEmpty()) {
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
