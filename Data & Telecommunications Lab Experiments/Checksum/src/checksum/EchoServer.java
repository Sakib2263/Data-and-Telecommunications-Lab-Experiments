/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checksum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Tanjid Hasan Tonmoy
 */
public class EchoServer {

    static int divisor = 16;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2200);

        System.out.println("Server waiting on port 2200");
        Socket clientSocket = serverSocket.accept();
        System.out.println(clientSocket + "\n");
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

        String inputLine;

        while (true) {
            inputLine = in.readUTF();

            //System.out.println("\nReceived from client " + inputLine);
            if (inputLine.equals("END")) {
                break;
            }
            String[] received = inputLine.split("##");
            String receivedSum = received[1];
            String message = received[0];
            int serverSum = 0;
            for (int i = 0; i < message.length(); i++) {
                serverSum += message.charAt(i);
            }

            int serverchecksum = serverSum % divisor;

            System.out.println("Received Message from client:\t" + message);

            if (serverchecksum == Integer.parseInt(receivedSum)) {
                out.writeUTF("Received");
                System.out.println("Success: checksum match, Response from server: Received\n");

            } else {
                out.writeUTF("Error");
                System.out.println("Error: Checksum mismatch , got: " + receivedSum + " calculated: " + serverchecksum);
                System.out.println("Response from server: Error\n");
            }

            //if(! (in.available() > 0)){
            //break;
            //}
            //System.out.println("/n/nFinished reading data ");
        }

        in.close();
        out.close();
    }
}
