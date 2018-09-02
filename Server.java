
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber=4444;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out =
            new PrintWriter(clientSocket.getOutputStream(), true);
//        BufferedReader in = new BufferedReader(
//            new InputStreamReader(clientSocket.getInputStream()));
        Scanner sc=new Scanner(System.in);
        String inputLine;
        while((inputLine=sc.nextLine())!=null){
            System.out.println(inputLine);
            out.println(inputLine);
        }
    }    
}
