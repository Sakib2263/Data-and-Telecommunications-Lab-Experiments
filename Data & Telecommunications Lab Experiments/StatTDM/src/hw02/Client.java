package hw02;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class Client {

    static String hostName = "172.16.13.211";
    static int portNumber = 2200;
    static int divisor = 16;
    private static int maxNoOfFile;

    public static void main(String[] args) throws IOException {
        Socket echoSocket = new Socket("127.0.0.1", portNumber);
        DataOutputStream out = new DataOutputStream(echoSocket.getOutputStream());
        DataInputStream in = new DataInputStream(echoSocket.getInputStream());
        
        maxNoOfFile = 5;
        
        FileReader[] fr = new FileReader[6];
        for (int i = 0; i < maxNoOfFile; i++) {
            int j = i+1;
            String fileName = "f" + j + ".txt";
            fr[i] = new FileReader(fileName);
        }
        
        //Scanner[] sc = new Scanner[6];
        BufferedReader[] br = new BufferedReader[maxNoOfFile];
        for (int i = 0; i < maxNoOfFile; i++) {
            br[i] = new BufferedReader(fr[i]);
        }
        
        Random randomGenerate = new Random();
        String startMarker = "@";
        String EndMarker = "%";
        //String[] Frame = new String[6];
        
            for (int i = 1; i < 6; i++) {
                StringBuilder finalFrame = new StringBuilder();
                finalFrame.append("^");
                //System.out.println("i = " + i);
                //String[] slot = new String[maxNoOfFile];
                
                for (int j = 0; j < maxNoOfFile; j++) {
                    
                    int randomNum = randomGenerate.nextInt(100);
                    if(randomNum > 50){
                        continue;
                    }
                    //System.out.println("j = " + j);
                    int bytes = 10;
                    char[] array = new char[bytes];
                    br[j].read(array, 0, 10);
                    
                    String data = new String(array);
                    
                    String src = "f" + (j+1);
                    String dst = src + "00";
                    
                    //System.out.println("Data = " + data);
                    
                    //String temp = startMarker + "#" + src + "#" + dst + "#" + slot[i] + "#" + EndMarker;
                    finalFrame.append(startMarker).append("#").append(src).append("#").append(dst);
                    finalFrame.append("#").append(data).append("#").append(EndMarker);
                    //System.out.println(Frame[i]);
                }
                finalFrame.append("^^");
                //String finalframe0 = "^"+ "%" + Frame[1] + "%" + Frame[2] + "%" + Frame[3] + "%" +Frame[4] + "%" +Frame[5] + "%" + "^^";
                System.out.println("\nFrame " + i + " : " + finalFrame);
                
                out.writeUTF(finalFrame.toString());
                out.flush();
            }        
            out.writeUTF("END");
        }
       // in.close();
       // out.close();

    }