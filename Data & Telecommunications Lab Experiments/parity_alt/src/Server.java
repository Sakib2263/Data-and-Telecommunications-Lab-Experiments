	import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

        
public class Server {

    private static Socket socket;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try {

            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25000");

            //Server is running always. This is done using this while(true) loop

            socket = serverSocket.accept();

            DataInputStream din;

            DataOutputStream dout;
            din = new DataInputStream(socket.getInputStream());

            dout = new DataOutputStream(socket.getOutputStream());


            //Functions f = new Functions();


            while (true) {
                String s = din.readUTF();
                int n = Integer.parseInt(s);

                //int x = Client.bitcount(n);
                int x = Integer.bitCount(n);
                if (x % 2 == 1) {
                    n = n >> 1;
                    String st =  String.valueOf((char)n);

                    String filename = "output.txt";
                    FileWriter fw = new FileWriter(filename, true); //the true will append the new data
                    fw.write(st);//appends the string to the file
                    System.out.println(st);
                    fw.close();

                } else {
                    System.out.println("Error");
                }
                dout.writeUTF(" ");
            }

        } catch (Exception e) {
        }



    }


}
