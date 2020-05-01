import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Random;

public class SecondPlayer
{
  public static void main(String[] args) throws Exception
  {
     BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));

     System.out.println("Welcome to OONA! What is the IP address of the server you want to play on?");
     String address = keyRead.readLine();
     Socket socket = new Socket(address, 3000);

     OutputStream ostream = socket.getOutputStream();
     PrintWriter pwrite = new PrintWriter(ostream, true);

     InputStream istream = socket.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

     System.out.println("Successfully connected to the server! Before we start, what is your player name?");

     String player2 = keyRead.readLine();
     pwrite.println(player2);
     pwrite.flush();

     System.out.println();

     String player1 = receiveRead.readLine();

     System.out.println("Welcome " + player1 + " and " + player2 + "!");
     System.out.println(player1 + " goes first. Type 'exit' on your turn to quit.\n");


     //looping (game implementation)
     String receiveMessage, sendMessage;
     while(true) {

        if((receiveMessage = receiveRead.readLine()) != null) {
            System.out.println(receiveMessage);

        } else {
                break;
        }
        sendMessage = keyRead.readLine();
        pwrite.println(sendMessage);
        pwrite.flush();

        }
        receiveRead.close();
        istream.close();
        pwrite.close();
        ostream.close();
        keyRead.close();
        socket.close();
    }
}
