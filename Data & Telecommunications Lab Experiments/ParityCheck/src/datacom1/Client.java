package datacom1;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {
    static Socket echoSocket;
    static PrintWriter out;
    static BufferedReader stdIn;

    static String hostName = "172.16.13.211";
    static int portNumber = 3333;
    public static void main(String[] args) throws IOException{
        try {
    Socket echoSocket = new Socket("127.0.0.1", portNumber);
    out = new PrintWriter(echoSocket.getOutputStream());
    BufferedReader in
            = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
    stdIn = new BufferedReader(
                    new InputStreamReader(System.in));
    FileReader fr = new FileReader("input.txt");
    Scanner sc = new Scanner(fr);
    
    while(sc.hasNextLine()){
        String line = sc.nextLine();
        char[] charArray = new char[100];
        charArray  =  line.toCharArray();
                
        for(int i=0; i<charArray.length; i++){
            int ascii = (int) charArray[i];
            int bitCount = Integer.bitCount(ascii);
            if(bitCount % 2 == 0){
                int temp = ascii << 1;
                ascii = temp | 1;
            }
            else{
                ascii = ascii | 0;
            }
            //out.writeInt(ascii);
            System.out.println(Integer.toBinaryString(ascii));
            charArray[i] = (char) ascii;
            System.out.println(charArray[i]);
        }
        out.write(charArray);
        out.flush();
    }          
    //out.append("hello");
    //out.flush();
    //out.write("Hello");
   // System.out.println(in.readLine());
            }
        catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }        finally{
            out.close();        
        }
        //echoSocket.close();
    }
}
