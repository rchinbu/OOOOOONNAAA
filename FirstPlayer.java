import java.io.*;
import java.net.*;
public class FirstPlayer
{
  public static void main(String[] args) throws Exception
  {
      ServerSocket server = new ServerSocket(3000);
      System.out.println("Waiting for a second player...");
      Socket sock = server.accept( );
      System.out.println("Player connected! Ready to play.");

                              // reading from keyboard (keyRead object)
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
                              // sending (pwrite object)
      OutputStream ostream = sock.getOutputStream();
      PrintWriter pwrite = new PrintWriter(ostream, true);

                              // receiving (receiveRead object)
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

      String receiveMessage, sendMessage;
      while(true) {

        sendMessage = keyRead.readLine();  // keyboard reading
        pwrite.println(sendMessage);       // sending to first player
        pwrite.flush();                    // flush the data
        if((receiveMessage = receiveRead.readLine()) != null) {
            System.out.println(receiveMessage);
        } else {
                receiveRead.close();
                istream.close();
                pwrite.close();
                ostream.close();
                keyRead.close();
                sock.close();
                server.close();
                System.out.println("Connection closed");
                break;
        }
      }
   }
}
