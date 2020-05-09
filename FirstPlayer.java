\import java.io.*;
import java.net.*;
import java.util.*;

public class FirstPlayer {

        public static void main(String[] arg) throws Exception {

                ArrayList<Player> players = new ArrayList<>();

                //Gets keyboard input from the user
                Scanner keyboard = new Scanner(System.in);
                System.out.println("Welcome to OONA! What is your player name?");
                String player1 = keyboard.nextLine();
                System.out.println();
                players.add(new Player(player1));

                //Initialize the server
                ServerSocket server = new ServerSocket(3000);
                System.out.println("Waiting for a second player...\n"); //

                Socket pipe = server.accept();
                System.out.println("Player connected!"); //error handling

                //input from client
                ObjectInputStream serverInputStream = new ObjectInputStream(pipe.getInputStream());

                //output TO client
                ObjectOutputStream serverOutputStream = new ObjectOutputStream(pipe.getOutputStream());

                //////////////////////////Game////////////////////////

                String player2 = (String)serverInputStream.readObject();
                players.add(new Player(player2));
                Deck gamePiles = new Deck();

                serverOutputStream.writeObject(players);

                //cards for "home" player
                for (int i = 0; i < 7; i ++) {
                        players.get(0).giveCard(gamePiles.draw());
                }

                                //pass out to other players
                for (int i = 0; i < 7; i++) {
                        serverOutputStream.writeObject(gamePiles.draw());
                }

                System.out.println("Welcome, " + players.get(0).name + " and " + players.get(1).name + ", let's play OONA!\n");

                System.out.println("Remember, this is not UNO. The way to win OONA is by playing the UNO the way everybody ACTUALLY plays, not by the point system.\nYou go around in a clockwise fashion, laying down a card that matches either the number or the color of the one on top of the discard pile. If you don't have any valid cards, you automatically have to draw another. You will have the choice to put down the newly drawn card if it is valid. If not, it will simply get added to your hand. The first player to run out of cards is the winner!\n");

                gamePiles.discardCard(gamePiles.draw());

                Color firstColor = gamePiles.topDiscardCard().getColor();
                serverOutputStream.writeObject(firstColor);

                Number firstNumber = gamePiles.topDiscardCard().getNumber();
                serverOutputStream.writeObject(firstNumber);

                if (firstNumber == Number.DRAW2) {
                        for (int i = 0; i < 2; i++) {
                                serverOutputStream.writeObject(gamePiles.draw());
                        }
                } else if (firstNumber == Number.DRAW4) {
                        for (int i = 0; i < 4; i++) {
                                serverOutputStream.writeObject(gamePiles.draw());
                        }
                }

                if (firstColor == Color.WILD) {
                        System.out.println("First card is a wild card! " + players.get(1).name + " must pick a color.");
                        firstColor = (Color)serverInputStream.readObject();
                        System.out.println("They chose: " + firstColor);
                } else if (firstNumber == Number.REVERSE) {
                        System.out.println("First card is reverse! Forfeit your turn.\n");
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);
                Player player = gameState.getTurn();

                Boolean hasWinner = false;
                String winner = "none";

                //int n = 0;
                while (!hasWinner) {
                        if (player == players.get(0)) {
                                System.out.println("--------------------\nHi " + player.name + "! It's your turn.\n");
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw(gamePiles.draw(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                serverOutputStream.writeObject(playedCard);
                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
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
                                serverOutputStream.writeObject(col);

                                Number num = gameState.getNumber();
                                serverOutputStream.writeObject(num);
                                if (num == Number.DRAW2) {
                                        for (int i = 0; i < 2; i++) {
                                                serverOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (num == Number.DRAW4) {
                                        for (int i = 0; i < 4; i++) {
                                                serverOutputStream.writeObject(gamePiles.draw());
                                        }
                                }
                        } else {
                                System.out.println("--------------------\nIt's " + player.name + "'s turn right now. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "\n");
                                Card playedCard = (Card)serverInputStream.readObject();
                                if (playedCard == null) {
                                        serverOutputStream.writeObject(gamePiles.draw());
                                }
                                playedCard = (Card)serverInputStream.readObject();

                                if (playedCard != null) {
                                        gamePiles.discardCard((Card)serverInputStream.readObject());
                                }

                                Color col = (Color)serverInputStream.readObject();
                                Number num = (Number)serverInputStream.readObject();
                                if (playedCard != null) {
                                        if (playedCard.getColor() == Color.WILD) {
                                                System.out.println("Card played was wild. " + player.name + " chose " + col + "\n");
                                        }
                                }
                                if (num == Number.DRAW2) {
                                        System.out.println("Card played was DRAW2! Draw 2 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 2; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                                }
                                } else if (num == Number.DRAW4) {
                                        System.out.println("Card played was DRAW4! Draw 4 cards and forfeit your turn.\n");
                                        for (int i = 0; i < 4; i++) {
                                                players.get(0).giveCard(gamePiles.draw());
                                                }
                                } else if (num == Number.SKIP) {
                                        System.out.println("Card played was SKIP! Forfeit your turn.\n");
                                }

                                player = gameState.update(col, num);
                        }
                        //n++;
                        //serverOutputStream.writeObject(n);
                        //serverOutputStream.writeObject(players.get(0).hand.size());
                        int player1size = (int)serverInputStream.readObject();
                        if (players.get(0).hand.size() == 0) {
                                hasWinner = true;
                                winner = players.get(0).name;
                        } else if (player1size == 0) {
                                hasWinner = true;
                                winner = players.get(1).name;
                        }

                        serverOutputStream.writeObject(hasWinner);
                        serverOutputStream.writeObject(winner);
                }
                if (winner.equals(players.get(0).name)) {
                        System.out.println("\nCongratulations! You have gained the prestigious title of OONA winner, " + winner + "!");
                } else if (winner.equals(players.get(1).name)) {
                        System.out.println("\nSorry, you suck. The winner is " + players.get(1).name + ".");
                }
                serverOutputStream.close();
                serverInputStream.close();
        }
}
