import java.io.*;
import java.net.*;
import java.util.*;

public class FirstPlayer {

        public static void main(String[] arg) throws Exception {

                //Get keyboard input from the user
                Scanner keyboard = new Scanner(System.in);

                System.out.println("Welcome to OONA! What is your player name?");
                String player2 = keyboard.nextLine();

                System.out.println("\nPerfect! What is the IP address of the server you want to play on?");
                String address = keyboard.nextLine();
                System.out.println();

                //Connect to the server
                Socket socket = new Socket (address, 3000); //error handling
                System.out.println("Successfully connected to the server!");

                //output TO server
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(socket.getOutputStream());

                //output from server
                ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());

                /////////////////////////////////Game/////////////////////////////

                clientOutputStream.writeObject(player2);

                @SuppressWarnings("unchecked")
                ArrayList<Player> players = (ArrayList)clientInputStream.readObject();

                //cards for "home" player
                for (int i = 0; i < 7; i++) {
                        Card card = (Card)clientInputStream.readObject();
                        players.get(1).giveCard(card);
                }
                System.out.println("Welcome, " + players.get(0).name + " and " + players.get(1).name + ", let's play OONA!\n");

                System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

                Color firstColor = (Color)clientInputStream.readObject();
                Number firstNumber = (Number)clientInputStream.readObject();

                if (firstNumber == Number.DRAW2) {
                        System.out.println("First card is DRAW2! Draw 2 cards.\n");
                        for (int i = 0; i < 2; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                } else if (firstNumber == Number.DRAW4) {
                        System.out.println("First card is DRAW4! Draw 4 cards.\n");
                        for (int i = 0; i < 4; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                }

                if (firstColor == Color.WILD) {
                        firstColor = players.get(1).chooseColor(keyboard);
                        clientOutputStream.writeObject(firstColor);
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is reverse! " + players.get(0).name + " forfeits their turn.\n");
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

                Boolean hasWinner = false;
                String winner = "none";

                while(!hasWinner) {
                        if (player == players.get(1)) {
                                System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                clientOutputStream.writeObject(playedCard);

                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw((Card)clientInputStream.readObject(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                clientOutputStream.writeObject(playedCard);

                                if (playedCard != null) {
                                        clientOutputStream.writeObject(playedCard); //discard
                                        player.removeCard(playedCard);
                                        if (playedCard.getColor() == Color.WILD) {
                                                Color newcolor = player.chooseColor(keyboard);
                                                player = gameState.update(newcolor, playedCard.getNumber());
                                        } else {
                                                player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                        }
                                } else if (playedCard == null) {
                                        player = gameState.update(null, null);
                                }
                                Color col = gameState.getColor();
                                clientOutputStream.writeObject(col);
                                Number num = gameState.getNumber();
                                clientOutputStream.writeObject(num);
                        } else {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)clientInputStream.readObject();
                                Color col = (Color)clientInputStream.readObject();
                                Number num = (Number)clientInputStream.readObject();
                                if (playedCard != null) {
                                        if (playedCard.getColor() == Color.WILD) {
                                                System.out.println("Card played was wild. " + player.name + " chose " + col + "\n");
                                        }
                                }
                                if (num == Number.DRAW2) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                Card card = (Card)clientInputStream.readObject();
                                                players.get(1).giveCard(card);
                                        }
                                } else if (num == Number.DRAW4) {
                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                Card card = (Card)clientInputStream.readObject();
                                                players.get(1).giveCard(card);
                                        }
                                } else if (num == Number.SKIP) {
                                        System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                }
                                player = gameState.update(col, num);
                        }
                        //int n = (int)clientInputStream.readObject();
                        //if (n == 2) {
                        //      hasWinner = true;
                        //}
                        clientOutputStream.writeObject(players.get(1).hand.size());
                        hasWinner = (Boolean)clientInputStream.readObject();
                        winner = (String)clientInputStream.readObject();
                }
                if (winner.equals(players.get(1).name)) {
                        System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                } else if (winner.equals(players.get(0).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(1).name + ".");
                }

                clientOutputStream.close();
                clientInputStream.close();
        }
}
