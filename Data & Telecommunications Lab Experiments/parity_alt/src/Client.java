import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {

    private static Socket socket;

    public static void main(String args[]) {

        Scanner scan = new Scanner(System.in);
        try {
            String host = "localhost";
            int port = 25000;
            socket = new Socket(host, port);

            DataInputStream din;

            DataOutputStream dout;
            din = new DataInputStream(socket.getInputStream());

            dout = new DataOutputStream(socket.getOutputStream());
            //Functions f = new Functions();
            Scanner input = new Scanner(System.in);
            File file = new File("input.txt");
            input = new Scanner(file);

            //C:\Users\student1\Downloads\SS\NetBeansProjects\Java Exam\input.txt
            while (input.hasNextLine()) {
                String s = input.nextLine();

                int len = s.length();

                char[] ch = new char[100];
                ch = s.toCharArray();
                for (int i = 0; i < len; i++) {
                    int n = (int) ch[i];
                    //int x = f.toBinary(n);
                   int x = Integer.bitCount(n);
                    n = n << 1;
                    if (x % 2 == 0) {
                        Random rg = new Random();
                        int a = rg.nextInt() % 3 + 1;
                        //if(a!=2)
                            n = n | 1;
                    }
                    String str = Integer.toString(n);
                    System.out.println(str + " ->" + (char) n);
                    dout.writeUTF(str);
                    String message = din.readUTF();
                }

            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public static int bitcount(int n){
        int c = 0;
        while(n>0)
        {
            int x = n%2;
            if(x==1)
                c++;
            n = n/2;
        }

        return c;

    }
    
}