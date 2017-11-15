package crc;

import java.io.*;
import java.net.*;
import java.util.Random;

public class MyClient {
    
    static Random random = new Random();

    public static void main(String[] args) throws IOException {
        File file = new File("input.txt");
        StringBuilder stringBuffer;
        String Pstring = "", Pstring1 = "", Pstring2 = "";
        Socket s = new Socket("localhost", 6666);
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int i, j;
                String sumString = "";
                int num[] = null;
                for (i = 0; i < line.length(); i++) {
                    int n = line.charAt(i);
                    String bstring = Integer.toBinaryString(line.charAt(i));
                    sumString += bstring;             
                }
                
                for (i = 0; i < sumString.length(); i+=26 ) {
                    String finalString = "";
                    for (j = i; j < 26+i; j++) {
                        if(j == sumString.length()) break;
                        finalString += sumString.charAt(j);
                    }
                    System.out.println("\nBefore Encoding: " + finalString);
                    Pstring = CrcUtils.getEncodedStringWithCrc(finalString);
                    
                    int randomNum = random.nextInt(1000);
                    if(randomNum % 3 == 0){
                        Pstring = addOneBitError(Pstring);
                    }
                    else if(randomNum % 5 == 0 ){
                        Pstring = add16BurstError(Pstring);
                    }
                    else if(randomNum % 9 == 0){
                        Pstring = add18BurstError(Pstring);
                    }
                    System.out.println("Sent encoded: " + Pstring);
                    dout.writeUTF(Pstring);
                    dout.flush();

                }
            }

            dout.writeUTF("stop");
            dout.flush();
        }
    }
    
    
    static String addOneBitError(String Pstring){
        char[] myNameChars = Pstring.toCharArray();
        myNameChars[7] = toggleBit(myNameChars[7]);
        return new String(myNameChars);
     
    }
    static String add16BurstError(String Pstring){
        char[] myNameChars = Pstring.toCharArray();
        for (int i = 0; i < 16; i++) {
            if(random.nextInt(16) %2 == 1 )
                   myNameChars[i] = toggleBit(myNameChars[i]);
               }
        return new String(myNameChars);
    }
    static String add18BurstError(String Pstring){
        char[] myNameChars = Pstring.toCharArray();
        for (int i = 0; i < 18; i++) {
            if(random.nextInt(16) %2 == 1 )
                   myNameChars[i] = toggleBit(myNameChars[i]);
               }
        return new String(myNameChars);
    }

    private static char toggleBit(char ch) {
        if(ch == '0')
            return '1';
        else return '0';
    }
}
