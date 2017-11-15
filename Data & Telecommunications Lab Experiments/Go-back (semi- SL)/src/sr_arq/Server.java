package sr_arq;
import java.io.*;
import java.net.*;
import java.util.*;


class Timer extends Thread{
    long st;
    int sqn;
    Timer(int _sqn){
        sqn = _sqn;
    }
    public void run(){
        try{
            st = System.currentTimeMillis();
            System.out.println("Timer " + sqn + " started");
            while(true){
                if(Server.state[sqn]==true) return;
                int x = (int) System.currentTimeMillis();
                x -= st;
                x = Math.abs(x);
                if(x>=300){
                    System.out.println("Timeout " + sqn);
                    Server.timeOut[sqn] = true;
                    return;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}


class Receiver extends Thread{

    public ServerSocket server;

    Receiver(ServerSocket _server){
        server = _server;
    }

    public void run(){
        Socket s = new Socket();
        DataInputStream dis;
        try{
            while(true){
                s = server.accept();
                dis = new DataInputStream(s.getInputStream());
                String lala = dis.readUTF();
                s.close();

                if(lala.length()<9) System.exit(0);
                else if(lala.charAt(8)=='1'){
                    int x = 0;
                    for(int i=0;i<8;i++){
                        x *= 2;
                        x += lala.charAt(i)-'0';
                    }
                    Server.state[x] = true;
                }
                else if(lala.charAt(8)=='0'){
                    int x = 0;
                    for(int i=0;i<8;i++){
                        x *= 2;
                        x += lala.charAt(i)-'0';
                    }
                    Server.state[x] = true;
                    Server.fmn[Server.now] = x;
                    int y = Server.crf;
                    int z = Server.sf;
                    while(x!=z){
                        y += 8;
                        z++;
                        z %= (2*Server.sw);
                    }
                    Server.st[Server.now++] = y;
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


class Sender extends Thread{
    public ServerSocket server;

    Sender(ServerSocket _server){
        server = _server;
    }
    public void run(){
        try{
            Socket s = new Socket();

            DataOutputStream dos;

            s = server.accept();
            dos = new DataOutputStream(s.getOutputStream());
            dos.write(Server.data.length()/8);
            s.close();

               System.out.println(Server.sf + " "+ Server.sn);
            while(true){
                //System.out.println(Server.sf + " "+ Server.sn);

                /*Selective Repeat Part*/

                while(Server.now!=0){
                    int x = Server.fmn[Server.now-1];
                    int y = Server.st[Server.now-1];

                    String frame = "";
                    for(int i=y;i<y+8;i++){
                        String temp = Integer.toString((int)Server.data.charAt(i), 2);
                        while(temp.length()<8) temp = "0"+temp;
                        frame += temp;
                    }
                    //frame = frame + CRCGeneratorS.generate(frame);
                    String temp = Integer.toString(x, 2);
                    while(temp.length()<8) temp = "0"+temp;
                    frame = temp+frame;

                    s = server.accept();
                    dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(frame);
                    s.close();
                    
                    Server.state[x] = false;
                    Server.timeOut[x] = false;
                    Timer tm = new Timer(x);
                    tm.start();

                    Server.now--;
                }

                /*Selective Repeat Part*/

                if(Server.crf >= Server.data.length()){
                    System.exit(0);
                }
                /*Window Sliding Starts*/
                int i = Server.sf;
                while(i!=Server.sn){
                    if(Server.crf >= Server.data.length()){
                        System.exit(0);
                    }
                    i %= (2*Server.sw);
                    if(Server.state[i] == true){
                        Server.state[i] = false;
                        Server.timeOut[i] = false;
                        Server.sf++;
                        Server.sf %= (2*Server.sw);
                        Server.crf += 8;

                    }
                    else break;
                    i++;
                    i %= (2*Server.sw);
                }
                /*Window Sliding Ends*/

                /*Regular Sending Starts*/
                boolean flag = false;
                while( (Server.sn - Server.sf + (2*Server.sw))%(2*Server.sw) < Server.sw){
                    int it = Server.crn;
                    if(it >= Server.data.length()) break;
                    String frame = "";

                    for(int j=it;j < it+8 && j < Server.data.length();j++){
                        String temp = Integer.toString((int)Server.data.charAt(j), 2);
                        while(temp.length()<8) temp = "0"+temp;
                        frame += temp;
                    }
                    //frame = frame + CRCGeneratorS.generate(frame);
                    String temp = Integer.toString(Server.sn, 2);
                    while(temp.length()<8) temp = "0"+temp;
                    frame = temp+frame;


                    /*Frame will not be sent to the receiver*/
                    Random rn = new Random();
                    int rand = rn.nextInt() % 100;
                    rand += 100;
                    rand %= 100;
                    Server.state[Server.sn] = false;
                    Server.timeOut[Server.sn] = false;

                    Timer tm = new Timer(Server.sn);
                    tm.start();
                    
                    if(rand>80){
                        System.out.println("Packet " + Server.sn + " is not sent.");
                        Server.sn++;
                        Server.sn %= (2*Server.sw);
                        Server.crn += 8;
                        
                        continue;
                    }
                    flag = true;

                    /*Frame will not be sent to the receiver*/

                    s = server.accept();
                    dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(frame);
                    s.close();

                    System.out.println("Packet " + Server.sn + " is successfully sent.");

                    Server.sn++;
                    Server.sn %= (2*Server.sw);
                    Server.crn += 8;
                    
                    System.out.println(Server.sf + " " + Server.sn);

                }
                /*Regular Sending Ends*/


                /*Lost Acknowledgement Case Starts*/
                
                int cur = Server.sf;
                for(i=Server.crf;i<Server.crn;i+=8){
                    if(Server.timeOut[cur]==false) continue;
                    cur %= (2*Server.sw);
                    String frame = "";
                    for(int j=i;j < i+8 && j < Server.data.length();j++){
                        String temp = Integer.toString((int)Server.data.charAt(j), 2);
                        while(temp.length()<8) temp = "0"+temp;
                        frame += temp;
                    }
                    String temp = Integer.toString(cur, 2);
                    while(temp.length()<8) temp = "0"+temp;
                    frame = temp+frame;
                    


                    Random rn = new Random();
                    int rand = rn.nextInt() % 100;
                    rand += 100;
                    rand %= 100;
                    Server.state[cur] = false;
                    Server.timeOut[cur] = false;

                    Timer tm = new Timer(cur);
                    tm.start();
                    
                    if(rand>80){
                        System.out.println("Frame " + cur + " is not sent again.");
                        cur++;
                        cur %= 16;
                        continue;
                    }
 

                    s = server.accept();
                    dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(frame);
                    s.close();
                    

                    System.out.println("Frame " + cur + " is sent again.");

                    cur++;
                    cur %= (2*Server.sw);
                }
                
                
                /*Lost Acknowledgement Case Ends*/
            }
        }catch(Exception e){

        }
    }
}


public class Server{
    public static String data = "";
    public static int sw = 8,sf = 0,sn = 0,crf = 0,crn = 0,now = 0;
    public static boolean[] state = new boolean[100];
    public static boolean[] timeOut = new boolean[100];
    public static int[] fmn = new int[100000];
    public static int[] st = new int[100000];

    public static void main(String[] args) throws IOException{
        try{
            /*Reading From File Starts*/
            FileInputStream f = new FileInputStream("input.txt");

            byte bytes[] = new byte[100];
            int read_count;
            while(true){
                read_count = f.read(bytes);
                for(int i=0;i<read_count;i++){
                    data += (char)bytes[i];
                }
                if(read_count<=0) break;
            }
            /*Reading From File Ends*/

            /*Initialization*/
            for(int i=0;i<100;i++) timeOut[i] = state[i] = false;


            ServerSocket server = new ServerSocket(8888);

            Receiver r = new Receiver(server);

            server = new ServerSocket(9999);

            Sender s = new Sender(server);

            r.start();
            s.start();
        }catch(Exception e){

        }
    }
}