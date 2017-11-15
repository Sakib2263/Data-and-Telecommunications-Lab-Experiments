/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checksum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Tanjid Hasan Tonmoy
 */
public class EchoClient {

    static String hostName = "172.16.13.211";
    static int portNumber = 2200;
    static int divisor = 16;

    public static void main(String[] args) throws IOException {
        Socket echoSocket = new Socket("127.0.0.1", portNumber);
        DataOutputStream out = new DataOutputStream(echoSocket.getOutputStream());
        DataInputStream in = new DataInputStream(echoSocket.getInputStream());
        FileReader fr = new FileReader("input.txt");
        Scanner sc = new Scanner(fr);

        Random random = new Random();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            int sum = 0;

            for (int i = 0; i < line.length(); i++) {

                sum += line.charAt(i);
            }
            
            int checkSum = sum % divisor;
            
            int randomIncoreectFacror = random.nextInt(100);
            if (randomIncoreectFacror < 30) {
                System.out.println("Random : Checkum changed, original " + checkSum + "  Given " + randomIncoreectFacror);
                checkSum = randomIncoreectFacror;
            }

            line = line + "##" + checkSum;
            out.writeUTF(line);
            System.out.println("Sent to server:\t" + line);
            String response = in.readUTF();
            if (response == null || response.length() == 0) {
                out.writeUTF(line);
            }
            System.out.println("Response from server:  " + response + "\n");

        }
        
        out.writeUTF("END");

        in.close();
        out.close();

    }

}
