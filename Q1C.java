import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
/**
*
* @author T'jon
*/
public class Q1C{
    public static void main(String[] args) throws Exception {

        try (Socket socket = new Socket("localhost",14147)){

Scanner scan = new Scanner(System.in);
//in&output streams
            OutputStream msgOut = socket.getOutputStream();
            InputStream msgIn = socket.getInputStream();

   while(true){

//light sanitization
    String input = scan.nextLine();
        if(input.contains("\\") || input.contains("\"") || input.contains("\'")){
            System.out.println("Invalid characters, message will not be sent");
            continue;
        }

        else if(input.isEmpty()){
System.out.println("Enter a valid line!");
continue;
        }

        else if(input.matches(".")){
            break;
        }
//output
byte[] dataOut = new byte[input.length()];
    dataOut = input.getBytes(StandardCharsets.UTF_8);
    ByteArrayOutputStream bOut = new ByteArrayOutputStream(dataOut.length);
    bOut.write(dataOut);
    bOut.writeTo(msgOut);
    bOut.flush();

//waits for server response
while(msgIn.available() == 0){
Thread.sleep(10);
}

           //input
            int n = msgIn.available();
    byte[] dataIn = new byte[n];
    msgIn.read(dataIn, 0, n);
    String text = new String(dataIn);

        //prints output
            System.out.println("Server: " + text + "\n");
        
    }
   
      //disconnects  
System.out.println("Terminating");
socket.close();
//System.exit(0);
    }catch (IOException ioe) {
        System.err.println(ioe);
                throw ioe;
        }//*/
    }
}

