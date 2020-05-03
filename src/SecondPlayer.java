import java.io.*;
import java.net.*;
public class SecondPlayer
{
  public static void main(String[] args) throws Exception
  {
     Socket socket = new Socket("127.0.0.1", 3000);
                               // reading from keyboard (keyRead object)
     BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
                              // sending to first player (pwrite object)
     OutputStream ostream = socket.getOutputStream();
     PrintWriter pwrite = new PrintWriter(ostream, true);

                              // receiving from first player ( receiveRead  object)
     InputStream istream = socket.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

     System.out.println("Successfully connected to the server! Ready to play.");

     String receiveMessage, sendMessage;
     while(true)
     {
        sendMessage = keyRead.readLine();  // keyboard reading
        pwrite.println(sendMessage);       // sending to first player
        pwrite.flush();                    // flush the data
        if((receiveMessage = receiveRead.readLine()) != null) //receive from server
        {
            System.out.println(receiveMessage); // displaying at DOS prompt
           }
        }
    }
}
