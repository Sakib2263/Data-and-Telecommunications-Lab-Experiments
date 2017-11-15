package datacom3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

/**
 *
 * @author Tanjid Hasan Tonmoy
 */
public class Datacom3 {

    public static void main(String[] args) {
        
        int noOfFiles = 3;
        String[] spreadingCodes = {"0101","0011","0000"};
        
        FileReader[] fr = new FileReader[noOfFiles];
        BufferedReader[] br = new BufferedReader[noOfFiles];
        
        for (int i = 0; i < noOfFiles; i++) {
            String fileName = "f" + i + ".txt";
            try {
                fr[i] = new FileReader(fileName);
                br[i] = new BufferedReader(fr[i]);
            } catch (FileNotFoundException ex) {
                System.out.println(fileName + "Not found");
            }
        }
        
        String[] readString = new String[3];
        StringBuilder[] finalString = new StringBuilder[3];
        
        for (int i = 0; i < noOfFiles; i++) {
            int bytes = 10;
            char[] array = new char[bytes];
            try {
                br[i].read(array, 0, bytes);
                readString[i] = new String(array);
                System.out.println("Before spreading " + readString[i]);
                finalString[i] = spreadString(toBinaryString(readString[i]), spreadingCodes[i]);
                System.out.println("After spreading " + finalString[i]);
            } catch (IOException ex) {
                System.out.println("Reading failed");
            }
        }
        
        System.out.println("Out " + combineSignals(finalString[0], finalString[1], finalString[2]));
        
        /*
        String text = "Hello World!";
        System.out.println("Text: " + text);

        String binary = new BigInteger(text.getBytes()).toString(2);
        System.out.println("As binary: " + binary);
        
        System.out.println("After spreading " + spreadString(binary, spreadingCodes[0]));
        

        String text2 = new String(new BigInteger(binary, 2).toByteArray());
        System.out.println("As text: " + text2);
*/
    }
    
    public static StringBuilder spreadString(String binary, String spreadCode){
        StringBuilder stringAfterSpread = new StringBuilder();
        for(int i = 0; i< binary.length()-1; i = i+2){
            char char1 = binary.charAt(i);
            stringAfterSpread.append(spreadOneBit(spreadCode, char1));
            char char2 = binary.charAt(i+1);
            stringAfterSpread.append(spreadOneBit(spreadCode, char2));   
        }
        return stringAfterSpread;
    }
    
    public static StringBuffer spreadOneBit(String spreadCode, char c){
        StringBuffer spreadString = new StringBuffer();
        for(int j =0; j<4; j++){
                if(c == spreadCode.charAt(j)){
                    //same, xor 0, encode +1
                    spreadString.append("0");
                }
                else{
                    spreadString.append("1");
                }
            }
        return spreadString;
    }
    
     public static String toBinaryString(String text){
         return new BigInteger(text.getBytes()).toString(2);
    }
     
     public static StringBuilder combineSignals(StringBuilder s1, StringBuilder s2, StringBuilder s3){
         StringBuilder str = new StringBuilder();
         for(int i= 0; i< s1.length(); i++){
             int temp = 0;
             temp+=combineHelper(s1.charAt(i));
             temp+=combineHelper(s2.charAt(i));
             temp+=combineHelper(s3.charAt(i));
             if(temp>=0){
                 str.append("+");
             }
             str.append(String.valueOf(temp));
         }
         return str;
    }
     
     public static int combineHelper(char ch){
         if(ch == '0'){
                 return 1;
             }
             else{
                 return -1;
             }
     }

}
