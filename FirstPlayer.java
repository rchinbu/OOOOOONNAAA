import java.io.*;
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

                if (firstColor == Color.WILD) {
                        firstColor = (Color)serverInputStream.readObject();
                }
                if (firstNumber == Number.DRAW2) {
                        for (int i = 0; i < 2; i++) {
                                serverOutputStream.writeObject(gamePiles.draw());
                        }
                }
                if (firstNumber == Number.DRAW4) {
                        for (int i = 0; i < 4; i++) {
                                serverOutputStream.writeObject(gamePiles.draw());
                        }
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);
//              serverOutputStream.writeObject(gameState);

//              String receiveMessage, sendMessage;
//                while(true) {
//                      sendMessage = keyboard.nextLine();
//                      serverOutputStream.writeObject(sendMessage);
//                      if (sendMessage.equals("exit")) {
//                              break;
//                      }
                        /////////////////////////////////////////////////////
//                      receiveMessage=(String)serverInputStream.readObject();
//                      if (receiveMessage.equals("exit")) {
//                      break;
//                      }
//                      System.out.println(receiveMessage);
//               }

                Player player = gameState.getTurn();
                serverOutputStream.writeObject(player);

                serverOutputStream.writeObject(gameState.getColor());
                serverOutputStream.writeObject(gameState.getNumber());

                Boolean hasWinner = false;
                String winner = "none";
                while(!hasWinner) {
                        if (player == players.get(0)) {
                                System.out.println("Hi, " + player.name + "! It's your turn.\n");
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw(gamePiles.draw(), gameState.getColor(), gameState.getNumber(), keyboard);
                                }
                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
                                        player.removeCard(playedCard);
                                        if (playedCard.getColor() == Color.WILD) {
                                                Color newcolor = player.chooseColor(keyboard);
                                                player = gameState.update(newcolor, playedCard.getNumber());
                                        } else {
                                                player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                        }
                                } else {
                                        player = gameState.update(null, null);
                                }

                                serverOutputStream.writeObject(player);
                                serverOutputStream.writeObject(gameState);
                                serverOutputStream.writeObject(gameState.getColor());
                                serverOutputStream.writeObject(gameState.getNumber());
                                if (gameState.getNumber() == Number.DRAW2) {
                                        for (int i = 0; i < 2; i++) {
                                                serverOutputStream.writeObject(gamePiles.draw());
                                        }
                                } else if (gameState.getNumber() == Number.DRAW4) {
                                        for (int i = 0; i < 4; i++) {
                                                serverOutputStream.writeObject(gamePiles.draw());
                                        }
                                }
                        } else {
                                System.out.println("It is currently " + player.name + "'s turn. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "...\n");
                                Card playedCard = (Card)serverInputStream.readObject();
                                if (playedCard == null) {
                                        serverOutputStream.writeObject(gamePiles.draw());
                                        playedCard = (Card)serverInputStream.readObject();
                                }

                                if (playedCard != null) {
                                        gamePiles.discardCard(playedCard);
                                }

                                player = gameState.update(playedCard.color, playedCard.number);
                                if (playedCard.getNumber() == Number.DRAW2) {
                                        for (int i = 0; i < 2; i++) {
                                                player.giveCard(gamePiles.draw());
                                        }
                                } else if (playedCard.getNumber() == Number.DRAW4) {
                                        for (int i = 0; i < 4; i++) {
                                                player.giveCard(gamePiles.draw());
                                        }
                                }
                                serverOutputStream.writeObject(player);
                        }

                        if (players.get(0).hand.isEmpty()) {
                                hasWinner = true;
                                winner = players.get(0).name;
                        } else if ((Boolean)serverInputStream.readObject()) {
                                hasWinner = true;
                                winner = players.get(1).name;
                        }
                        serverOutputStream.writeObject(hasWinner);
                        serverOutputStream.writeObject(winner);
                }

                if (winner.equals(players.get(0).name)) {
                        System.out.println("You're the winner of OONA!");
                } else {
                        System.out.println("I'm sorry, you lost! " + players.get(1).name + " is the winner.");
                }

                serverOutputStream.close();
                serverInputStream.close();
        }
}
