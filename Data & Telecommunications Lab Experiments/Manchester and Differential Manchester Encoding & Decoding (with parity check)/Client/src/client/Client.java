
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static String encodeManchester(String text){
        int i,j,sign,len=text.length();
        int[] ara=new int[100];
        String ret="";
        for(i=0;i<len;i+=2){
            if(text.charAt(i)=='+'){sign=1;}
            else{sign=-1;}
            ara[i/2]=sign*5;
        }
        for(i=0;i<len/2;i+=2){
            if(ara[i]==5&&ara[i+1]==-5){ret+="0";}
            else if(ara[i]==-5&&ara[i+1]==5){ret+="1";}
        }
        return ret;
    }
    static String encodeDManchester(String text){
        int i,sign,j,prv=5,len=text.length();
        int[] ara=new int[100];
        String ret="";
        for(i=0;i<len;i+=2){
            if(text.charAt(i)=='+'){sign=1;}
            else{sign=-1;}
            ara[i/2]=sign*5;
        }
        for(i=0;i<len/2;i+=2){
            if(ara[i]==(-1)*prv&&ara[i+1]==prv){ret+="0";}
            else if(ara[i]==prv&&ara[i+1]==(-1)*prv){ret+="1";prv=(-1)*prv;}
        }
        return ret;
    }
    static boolean checkParity(String text){
        boolean f=false;
        int i,j,cnt;
        for(i=0,cnt=0;i<text.length();i++){
            if(text.charAt(i)=='1'){cnt++;}
        }
        return cnt%2==0;
    }
    public static void main(String[] args) throws IOException {
        String hostName = "127.0.0.1";
        int portNumber = 4444,len,i,j;
        boolean chk=true;
        Socket socket = new Socket(hostName, portNumber);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        String data = "",encodedString;
        String encodedData="";
        System.out.println("Connected....");
        data=in.readLine();
        int chunkSize=Integer.parseInt(data);
//        System.out.println(chunkSize);
        for(j=0;j<chunkSize;j++){
//            System.out.println(data);
            data=in.readLine();
            encodedString=encodeManchester(data);
            len=encodedString.length();
            boolean f=checkParity(encodedString);
            chk=chk&f;
            for(i=0;i<len-1;i++){
                encodedData+=encodedString.charAt(i);
            }
        }
        System.out.println("Mannchester voltage input : "+ data);
        System.out.println("Manchester Decode: "+encodedData);
        encodedData="";
        for(j=0;j<chunkSize;j++){
//            System.out.println(data);
            data=in.readLine();
             System.out.println("Differential Mannchester voltage input : "+ data);
            encodedString=encodeDManchester(data);
            len=encodedString.length();
            boolean f=checkParity(encodedString);
            chk=chk&f;
            for(i=0;i<len-1;i++){
                encodedData+=encodedString.charAt(i);
            }
        }
        if(chk==false) System.out.println("Error Data. Please SEnd Again!");
        else System.out.println("Same Parity!");
        System.out.println("Differential manchester Decode: "+encodedData);
    }
    
}
