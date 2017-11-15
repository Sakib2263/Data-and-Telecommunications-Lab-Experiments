package datacom1;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    static BufferedReader in;

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(3333);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            char[] outArray = new char[in.read()];
            in.read(outArray);
            for (int i = 0; i < outArray.length; i++) {

                int value = (int) outArray[i];
                //System.out.println("Received " + value);
                int bitCount = Integer.bitCount(value);
                if (bitCount % 2 == 1) {
                    value = value >> 1;
                    outArray[i] = (char) value;
                }
                /*
                 else{
                 System.out.println("Error Occured");
                 }
                 */
            }
            inputLine = new String(outArray);
            System.out.println(inputLine);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            in.close();
        }
    }
}
