package hw02;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2200);

        System.out.println("Server waiting on port 2200");
        Socket clientSocket = serverSocket.accept();
        System.out.println(clientSocket + "\n");
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        int i, j;

        String inputLine;
        BufferedWriter bw = null;
        FileWriter fw = null;

        while (true) {
            inputLine = in.readUTF();
            if (inputLine.equals("END")) {
                //System.out.println(inputLine);
                break;
            }
            //System.out.println("\n\n"+inputLine);
            
            String[] received = inputLine.split("%");
            
           // System.out.println("Size " + received.length);
            
            for (i = 0; i < received.length; i++) {
                String[] data = received[i].split("#");
                if(data.length != 4){
                    continue;
                }
                String fname = data[2];
                String data1 = data[3];
                //data1 = data1 + "\n";
               // System.out.println("Data   " + data1);
                String filename = fname + ".txt";
                fw = new FileWriter(filename, true);
                fw.write(data1);
                fw.flush();
            }      
        } 
        for(i= 1;i<6;i++){
            String fname = "f" +(i);   
            File file = new File(fname+".txt");         
            System.out.println(file.length());
        }
        for(i= 1;i<6;i++){
            String fname = "f" +(i)+"00";   
            File file = new File(fname+".txt");         
            System.out.println(file.length());
        }
        //in.close();
        //out.close();
    }
}
