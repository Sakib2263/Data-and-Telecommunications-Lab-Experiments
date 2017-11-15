package crc;

import java.io.*;  
import java.net.*;  
public class MyServer {  
@SuppressWarnings("UnnecessaryContinue")
public static void main(String[] args) throws IOException{ 
    String x = " ";
    String line="";
    int j = 0;
    FileWriter fout=new FileWriter("out.txt",true);
   PrintWriter fileout = new PrintWriter(fout,true);    
    try{              
        try (ServerSocket ss = new ServerSocket(6666)) {
            Socket s=ss.accept();
            DataInputStream dis=new DataInputStream(s.getInputStream());
            //String str;
            while(true){
                 String  str=dis.readUTF();
                if(str.equals("stop"))
                {
                    break;
                }
                System.out.println(str);
                String hh= CrcUtils.verifyCRC(str);
                //fileout.println(str+" "+hh);
                System.out.println(hh + "\n");
                //System.out.println(str);               
            } 
            fout.close();              
        }
             
    }catch(IOException | NumberFormatException e){System.out.println(e);}
}
}
