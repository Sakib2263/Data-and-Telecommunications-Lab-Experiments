
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 4444;
        Socket socket = new Socket(hostName, portNumber);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        String data;
//            System.out.println("sajib\n");
        while((data=in.readLine())!=null){
            System.out.println("digital data:"+data);
            int ara[]=new int[100];
            for(int i=0;i<data.length();i++){
                if(data.charAt(i)=='0'){ara[i]=-5;}
                else{ara[i]=+5;}
            }
            System.out.println("nrz-l sequence");
            for(int i=0;i<data.length();i++){
                if(i!=0){System.out.print(" ");}
                System.out.print(ara[i]);
            }
            System.out.print("\n");
            int prv=5;
            for(int i=0;i<data.length();i++){
                if(data.charAt(i)=='0'){ara[i]=prv;}
                else{ara[i]=(-1)*prv;prv=-prv;}
            }
            System.out.println("nrz-i sequenc");
            for(int i=0;i<data.length();i++){
                if(i!=0){System.out.print(" ");}
                System.out.print(ara[i]);
            }
            System.out.print("\n");
            for(int i=0;i<data.length();i++){
                if(data.charAt(i)=='0'){
                    ara[2*i]=-5;
                    ara[2*i+1]=0;
                }
                else{
                    ara[2*i]=5;
                    ara[2*i+1]=0;
                }
            }
            System.out.println("rz sequence");
            for(int i=0;i<2*data.length();i++){
                if(i!=0){System.out.print(" ");}
                System.out.print(ara[i]);
            }
            System.out.print("\n");
        }
    }
    
}
