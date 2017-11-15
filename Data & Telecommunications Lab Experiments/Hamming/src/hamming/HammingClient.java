package hamming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class HammingClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",2213);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        
        FileReader fileReader = new FileReader("input.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
                   
            int val;
            while((val = bufferedReader.read())!= -1){
                
                String data = Integer.toBinaryString(val);
                while(data.length()!=8){
                    data = "0"+data;
                }
                System.out.println(data);
                String hamming = getSt(data);
                
                hamming = hamming1(hamming);
                hamming = hamming2(hamming);
                hamming = hamming4(hamming);
                hamming = hamming8(hamming);
                
                System.out.println(hamming);
                    StringBuilder sb=new StringBuilder(hamming);
                    int pos = (int) (Math.random()*100);
                    if(pos>50){
                        if(sb.charAt(7) == '0'){
                            sb.setCharAt(7, '1');
                        }
                        else{
                            sb.setCharAt(7, '0');
                        }
                        System.out.println("Error at " + 7);
                    }
                    char c = (char) val;
                    System.out.println("Sending : " + c);
                dataOutputStream.writeUTF(sb.toString());
                dataInputStream.readUTF();
                
            }
            
                dataOutputStream.writeUTF("-1");
            
        
        
    }
    
    public static String getSt(String in){
        String out = "00"+in.substring(0, 1)+"0"+in.substring(1, 4)+"0"+in.substring(4);
        
        return out;
    } 
    
    static String hamming1(String in){
        
        int [] ar = {3,5,7,9,11};
        int temp = 0;
        for (int i = 0; i < ar.length; i++) {
            int x=in.charAt(ar[i]-1);
            if( x== '1' ){
                temp++;
            }
        }
        
        if(temp%2==0){
            in = "0" + in.substring(1);
        } else{
            in = "1" + in.substring(1);
        }
        
        return in;        
    }
    
    static String printParity(String s){
        String out="";
        int[] ar = {0,1,3,7};
        for(int i=0;i<ar.length;i++){
            out +=  s.charAt(ar[i]);
        }
        
        return out;
    }
    
    static String hamming2(String in){
        
        int [] ar = {3,6,7,10,11};
        int temp = 0;
        StringBuilder output = new StringBuilder(in);
        for (int i = 0; i < ar.length; i++) {
            int x=in.charAt(ar[i]-1);
            if( x== '1' ){
                temp++;
            }
        }
        
        if(temp%2==0){
            output.setCharAt(1, '0');
        } else{
            output.setCharAt(1, '1');
        }
        
        return output.toString();
    }
    
    static String hamming4(String in){
        
        int [] ar = {5,6,7,12};
        int temp = 0;
        StringBuilder output = new StringBuilder(in);
        for (int i = 0; i < ar.length; i++) {
            int x=in.charAt(ar[i]-1);
            if( x== '1' ){
                temp++;
            }
        }
        
        if(temp%2==0){
            output.setCharAt(3, '0');
        } else{
            output.setCharAt(3, '1');
        }
        
        return output.toString();       
    }
    
    static String hamming8(String in){
        
        int [] ar = {9,10,11,12};
        int temp = 0;
        StringBuilder output = new StringBuilder(in);
        for (int i = 0; i < ar.length; i++) {
            int x=in.charAt(ar[i]-1);
            if( x== '0' ){
                temp++;
            }
        }
        
        if(temp%2==0){
            output.setCharAt(7, '0');
        } else{
            output.setCharAt(7, '1');
        }
        
        return output.toString();        
    }
}
    