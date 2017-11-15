package datacom3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Tanjid Hasan Tonmoy
 */
public class Demodulator_Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2200);

        System.out.println("Server waiting on port 2200");
        Socket clientSocket = serverSocket.accept();
        System.out.println(clientSocket + "\n");
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        
        String str = in.readUTF();

        //String str = "-3-1-1+1-1-3+1-1-1-3+1-1+1+3-1+1-3-1-1+1+1+3-1+1+1+3-1+1+3+1+1-1-3-1-1+1-3-1-1+1+3+1+1-1-3-1-1+1+3+1+1-1+1+3-1+1+1+3-1+1+3+1+1-1-1-3+1-1-3-1-1+1+3+1+1-1-3-1-1+1+1+3-1+1+3+1+1-1-1-3+1-1+3+1+1-1-1-3+1-1-3-1-1+1-1-3+1-1+3+1+1-1+3+1+1-1-1-3+1-1-1-3+1-1+3+1+1-1+1+3-1+1-3-1-1+1+1+3-1+1+3+1+1-1+1+3-1+1+3+1+1-1+3+1+1-1";
        int[] spread1 = {1,-1,1,-1};
        int[] spread2 = {1,1,-1,-1};
        int[] spread3 = {1,1,1,1};
        
        ArrayList<Integer> int_data = new ArrayList<Integer>();
        for(int i =0; i<str.length(); i+=2){
            int_data.add(Integer.parseInt(str.substring(i, i+2)));
        }
        
        StringBuilder s1 = getOne(int_data, spread1);
        StringBuilder s2 = getOne(int_data, spread2);
        StringBuilder s3 = getOne(int_data, spread3);
        
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        
        String fs1 = toActualString(s1);
        String fs2 = toActualString(s2);
        String fs3 = toActualString(s3);
        
        System.out.println(fs1);
        System.out.println(fs2);
        System.out.println(fs3);
        
        FileWriter fw1 = new FileWriter("f_out_1.txt");
        fw1.write(fs1);
        fw1.flush();
        
        FileWriter fw2 = new FileWriter("f_out_2.txt");
        fw2.write(fs2);
        fw2.flush();
        
        FileWriter fw3 = new FileWriter("f_out_3.txt");
        fw3.write(fs3);
        fw3.flush();

        in.close();
        out.close();
        fw1.close();
        fw2.close();
        fw3.close();
        }
    
    public static StringBuilder getOne(ArrayList<Integer> int_data, int [] spread){
        StringBuilder out = new StringBuilder();
        for(int i =0; i< int_data.size(); i+=4){
            int temp = 0;
            temp+=int_data.get(i) * spread[0];
            temp+=int_data.get(i+1) * spread[1];
            temp+=int_data.get(i+2) * spread[2];
            temp+=int_data.get(i+3) * spread[3];
            
            if(temp/4 == -1){
                out.append("1");
            }else{
                out.append("0");
            }
        }
        return out;
    }
    
    public static String toActualString(StringBuilder s){
        String text2 = new String(new BigInteger(s.toString(), 2).toByteArray());
        return text2;
    }
}
