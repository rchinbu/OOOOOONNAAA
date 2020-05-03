import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Random;

public class FirstPlayer
{
  public static void main(String[] args) throws Exception
  {
      Boolean hasWinner=false;
      String winner;
      Deck gamePiles = new Deck();
      ArrayList<Player> players = new ArrayList<>();

      //read keyboard input
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));

      ///////////////////////////////////////////////////////////////////

      System.out.println("Welcome to OONA! What is your player name?");
      String player1 = keyRead.readLine();

      System.out.println();
      players.add(new Player(player1));

      ServerSocket server = new ServerSocket(3000);
      System.out.println("Waiting for a second player...\n");

      Socket sock = server.accept();
      System.out.println("Player connected!");

      //////////////////////////////////////////////////////////////////

      //stream and writer that send TO client socket
      OutputStream ostream = sock.getOutputStream();
      PrintWriter pwrite = new PrintWriter(ostream, true);

      //stream and getter that received FROM client socket
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

      players.add(new Player(receiveRead.readLine()));

      System.out.println("Welcome " + players.get(0).name + " and "+ players.get(1).name + "!");
      pwrite.println(players.get(0).name);
      pwrite.flush();

        for (int i = 0; i < 7; i++) {
                for (Player player : players) {
                        player.giveCard(gamePiles.draw());
                }
        }



      //looping (game implementation)
      System.out.println("You go first. Type 'exit' on your turn to quit.\n");

      String receiveMessage, sendMessage;
      while(!((sendMessage=keyRead.readLine()).equals("exit"))) {

        //sendMessage = keyRead.readLine();  // keyboard reading
        pwrite.println(sendMessage);       // sending to second player
        pwrite.flush();                    // flush the data

        receiveMessage=receiveRead.readLine();

        if (receiveMessage.equals("exit")) {
                break;
        } else if (receiveMessage != null) {
            System.out.println(receiveMessage);
                }
        }
        receiveRead.close();
        istream.close();
        ostream.close();
        keyRead.close();
        sock.close();
        server.close();
   }
}

