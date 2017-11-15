package hamming;

/**
 *
 * @author Sakib
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.net.ServerSocket;
import java.net.Socket;


 public class HammingnServer {

    public static String toActualString(String s) {
        return new String(new BigInteger(s, 2).toByteArray());
    }
    /**
     * @param args the command line arguments
     */
    static int ar[][] = new int[20][20];

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket ss = new ServerSocket(2213);
        Socket echoSocket = ss.accept();
        DataOutputStream dout = new DataOutputStream(echoSocket.getOutputStream());
        DataInputStream din = new DataInputStream(echoSocket.getInputStream());
        FileWriter fout = new FileWriter("out.txt");
        while (true) {

            String input = din.readUTF();
            if (input.equals("-1")) {
                break;
            }
            System.out.println("input: " + input);
            int sum = 0;
            String newer = hamming(input);
            String old = extractParity(input);
            sum = getInt(old, newer);
            System.out.println("Error At: " + sum);
            if (sum != 0) {
                StringBuilder output = new StringBuilder(input);
                char ch = output.charAt(sum - 1);

                if (ch == '1') {
                    output.setCharAt(sum - 1, '0');
                } else {
                    output.setCharAt(sum - 1, '1');
                }
                input = output.toString();
            }
            String out = "";
            for (int i = 0; i < 12; i++) {
                if (i != 0 && i != 1 && i != 3 && i != 7) {
                    out = out + input.charAt(i);
                }
            }
            char ch = (char) Integer.parseInt(out, 2);
            System.out.println("Input binary:"+ out);
            System.out.println("Input:"+ ch);
            fout.write(ch);
            dout.writeUTF("");
        }
        fout.close();

    }

    static String hamming(String in) {

        int[] ar = {3, 5, 7, 9, 11};
        int temp = 0;
        StringBuilder output = new StringBuilder(in);
        for (int i = 0; i < ar.length; i++) {
            int x = in.charAt(ar[i] - 1);
            if (x == '1') {
                temp++;
            }
        }
 //System.out.println(temp);
        if (temp % 2 == 0) {
            output.setCharAt(0, '0');
        } else {
            output.setCharAt(0, '1');
        }
        temp = 0;
        int[] ar1 = {3, 6, 7, 10, 11};
        for (int i = 0; i < ar1.length; i++) {
            int x = in.charAt(ar1[i] - 1);
            if (x == '1') {
                temp++;
            }
        }
   //         System.out.println(temp);
        if (temp % 2 == 0) {
            output.setCharAt(1, '0');
        } else {
            output.setCharAt(1, '1');
        }
        temp = 0;
        int[] ar2 = {5, 6, 7, 12};
        for (int i = 0; i < ar2.length; i++) {
            int x = in.charAt(ar2[i] - 1);
            if (x == '1') {
                temp++;
            }
        }
 //System.out.println(temp);
        if (temp % 2 == 0) {
            output.setCharAt(3, '0');
        } else {
            output.setCharAt(3, '1');
        }
        temp = 0;
        int[] ar3 = {9, 10, 11, 12};
        for (int i = 0; i < ar3.length; i++) {
            int x = in.charAt(ar[i] - 1);
            if (x == '1') {
                temp++;
            }
        }
 //System.out.println(temp);
        if (temp % 2 == 0) {
            output.setCharAt(7, '0');
        } else {
            output.setCharAt(7, '1');
        }

        String out = "" + output.charAt(7) +""+ output.charAt(3) +"" + output.charAt(1) +"" + output.charAt(0);
        System.out.println("New Parity: " + out);
        return out;
    }

    static String extractParity(String in) {
        System.out.println("Old Parity: " + in.charAt(7) +"" + in.charAt(3) +"" + in.charAt(1) +"" + in.charAt(0));
        return "" + in.charAt(7)  +""+ in.charAt(3)  +""+ in.charAt(1) +"" + in.charAt(0);

    }

    static int getInt(String old, String newer) {
        String value = "";
        for (int i = 0; i < 4; i++) {
            if (old.charAt(i) == newer.charAt(i)) {
                value += '0';
            } else {
                value += '1';
            }

        }
        return Integer.parseInt(value, 2);
    }
}
