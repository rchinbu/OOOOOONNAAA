import java.io.*;
import java.net.*;
import java.util.*;

public class SecondPlayer {

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

                if (firstColor == Color.WILD) {
                        clientOutputStream.writeObject(keyboard.nextLine());
                }
                if (firstNumber == Number.DRAW2) {
                        for (int i = 0; i < 2; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                }
                if (firstNumber == Number.DRAW4) {
                        for (int i = 0; i < 4; i++) {
                                Card card = (Card)clientInputStream.readObject();
                                players.get(1).giveCard(card);
                        }
                }

                GameState gameState = new GameState(players, firstColor, firstNumber);

//                GameState gameState = (GameState)clientInputStream.readObject();

//                String receiveMessage, sendMessage;
//                while(true) {
//                        receiveMessage = (String)clientInputStream.readObject();
//                      if (receiveMessage.equals("exit")) {
//                              break;
//                      }
//                        System.out.println(receiveMessage);
//
//
//                        sendMessage = keyboard.nextLine();
//                        clientOutputStream.writeObject(sendMessage);
//                        if (sendMessage.equals("exit")) {
//                              break;
//                        }
//               }

                Player player = (Player)clientInputStream.readObject();
                Color col = (Color)clientInputStream.readObject();
                Number num = (Number)clientInputStream.readObject();

                Boolean hasWinner = false;
                String winner = "none";

                while(!hasWinner) {
                        if (player != players.get(1)) {
                                System.out.println("It is currently " + player.name + "'s turn. They are playing to " + gameState.getColor() + " " + gameState.getNumber() + "...\n");

                                //Card playedCard = (Card)clientInputStream.readObject();
                                player = (Player)clientInputStream.readObject();
                                gameState = (GameState)clientInputStream.readObject();
                                col = (Color)clientInputStream.readObject();
                                num = (Number)clientInputStream.readObject();
                                gameState.update(col, num);
                                if (num == Number.DRAW2) {
                                        for (int i = 0; i < 2; i++) {
                                                Card card = (Card)clientInputStream.readObject();
                                                players.get(1).giveCard(card);
                                        }
                                } else if (num == Number.DRAW4) {
                                        for (int i = 0; i < 4; i++) {
                                                Card card = (Card)clientInputStream.readObject();
                                                players.get(1).giveCard(card);
                                        }
                                }

//                              player = (Player)clientInputStream.readObject();
  //                              gameState = (GameState)clientInputStream.readObject();
    //                            col = (Color)clientInputStream.readObject();
      //                          num = (Number)clientInputStream.readObject();
        //                        gameState.update(col, num);
                        } else {
                                System.out.println("Hi, " + player.name + "! It's your turn.\n");
                                Card playedCard = player.playCard(gameState.getColor(), gameState.getNumber(), keyboard);
                                clientOutputStream.writeObject(playedCard);

                                if (playedCard == null) {
                                        playedCard = player.playAfterDraw((Card)clientInputStream.readObject(), gameState.getColor(), gameState.getNumber(), keyboard);
                                        clientOutputStream.writeObject(playedCard);
                                }

                                if (playedCard != null) {
                                        player.removeCard(playedCard);
                                        if (playedCard.getColor() == Color.WILD) {
                                                player = gameState.update(player.chooseColor(keyboard), playedCard.getNumber());
                                        } else {
                                                player = gameState.update(playedCard.getColor(), playedCard.getNumber());
                                        }
                                } else {
                                        player = gameState.update(null, null);
                                }

                                player = (Player)clientInputStream.readObject();
                        }
                        clientOutputStream.writeObject(players.get(1).hand.isEmpty());
                        hasWinner = (Boolean)clientInputStream.readObject();
                        winner = (String)clientInputStream.readObject();
                }

                if (winner.equals(players.get(1).name)) {
                        System.out.println("You're the winner!");
                } else {
                        System.out.println("I'm sorry, you lost! " + players.get(0).name + " is the winner.");
                }
                clientOutputStream.close();
                clientInputStream.close();
        }
}
