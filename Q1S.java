import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
/**
 *
 * @author T'jon
 */
public class Q1S{
     public static void main(String[] args) throws Exception {
            
            //try w/ resources
            try ( ServerSocket servSock = new ServerSocket(14147)){          
            Socket client = servSock.accept();       
                System.out.println("CLIENT CONNECTED");
                
                //in&output streams
                InputStream is = client.getInputStream();  
                OutputStream os = client.getOutputStream();
                 
            while(!client.isClosed()){//loops
               
                //input
               int n = is.available();
                 byte[] data = new byte[n];
                 is.read(data, 0, n);
                 String text = new String(data);

                //output
                ByteArrayOutputStream bOut = new ByteArrayOutputStream(data.length);
                bOut.write(data);
                bOut.writeTo(os);//problem here for some reason
                bOut.flush(); 
            }
           
            client.close();
            servSock.close();
                System.out.println("Client disconnected.\nShutting down.");      
           System.exit(0);
        }catch (IOException ioe) {
            System.err.println(ioe);
                    throw ioe;
        }
    }
    
}


