
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
    static String decodeManchester(String text){
        int i,j;
      
        String ret="";
        int[] ara=new int[100];
        int len=text.length();
        for(i=0;i<len;i++){
            if(text.charAt(i)=='0'){
                ara[2*i]=5;
                ara[2*i+1]=-5;
            }
            else{
                ara[2*i]=-5;
                ara[2*i+1]=5;
            }
        }
        for(i=0;i<2*len;i++){
            if(ara[i]==5){
                ret+="+5";
            }
            else{
                ret+="-5";
            }
        }
        System.out.println("Mannchester encode : "+ ret);
        return ret;
    }
    static String decodeDManchester(String text){
        int i,j,prv=5,len=text.length();
        int[] ara=new int[100];
        String ret="";
        for(i=0;i<len;i++){
            if(text.charAt(i)=='0'){
                ara[2*i]=(-1)*prv;
                ara[2*i+1]=prv;
            }
            else{
                ara[2*i]=prv;
                ara[2*i+1]=(-1)*prv;
                prv=(-1)*prv;
            }
        }
        for(i=0;i<2*len;i++){
            if(ara[i]==5){
                ret+="+5";
            }
            else{
                ret+="-5";
            }
        }
        System.out.println("Differential Mannchester encode : "+ ret);
        return ret;    
    }
    public static void main(String[] args) throws IOException {
        int portNumber=4444,i,j,chunkSize=0,cnt=0;
        String[] chunk=new String[100];
        String[] manChunk=new String[100];
        String[] dManChunk=new String[100];
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        System.out.println("Enter Your Digital input: ");
        PrintWriter out =
            new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        Scanner sc=new Scanner(System.in);
        String inputLine;
        inputLine=sc.nextLine();
//        System.out.println(inputLine);
        for(i=0;i<inputLine.length();i+=8){
            chunk[chunkSize]="";
            for(cnt=0,j=i;j<=i+7&&j<inputLine.length();j++){
                if(inputLine.charAt(j)=='1'){cnt++;}
                chunk[chunkSize]+=inputLine.charAt(j);
            }
            if(cnt%2==0){chunk[chunkSize]+="0";}
            else{chunk[chunkSize]+="1";}
            chunkSize++;
        }
        for(i=0;i<chunkSize;i++){
            manChunk[i]=decodeManchester(chunk[i]);
            dManChunk[i]=decodeDManchester(chunk[i]);
        }
        out.println(Integer.toString(chunkSize));
        //System.out.println("Manchester Encoded Voltage Data: ");
        for(i=0;i<chunkSize;i++){
//            System.out.println(inputL);
            out.println(manChunk[i]);
        }
        //System.out.println(" Differential Manchester Encoded Voltage Data: ");
        for(i=0;i<chunkSize;i++){
            out.println(dManChunk[i]);
        }
    }    
}
