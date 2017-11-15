package sr_arq;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
        
class TimerC extends Thread{
    long st;
    int sqn;
    TimerC(int _sqn){
        sqn = _sqn;
    }
    public void run(){
        try{
            st = System.currentTimeMillis();
            System.out.println("Timer " + sqn + " started");
            while(true){
                if(Client.state[sqn]==true) return;
                int x = (int) System.currentTimeMillis();
                x -= st;
                x = Math.abs(x);
                if(x>=300){
                    System.out.println("Timeout " + sqn);
                    Client.timeOut[sqn] = true;
                    return;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}


class Reciever extends Thread{

    int got = 0;
    public void run(){
        try{
            int cnt = 0;
            PrintWriter pw = new PrintWriter("output.txt");
            DataInputStream dis = null;
            DataOutputStream dos;
            String frame = "";
            int cur;
            Socket s = new Socket();
            s = new Socket(Client.localhost,9999);
            dis = new DataInputStream(s.getInputStream());
            Client.sz = dis.read();
            s.close();

            while(true){
                /*Window Sliding Starts*/
                while(Client.state[Client.rn] == true){
                    got++;
                    if(got==Client.sz){
                        s = new Socket(Client.localhost,8888);
                        dos = new DataOutputStream(s.getOutputStream());
                        dos.writeUTF("00");
                        s.close();
                        System.exit(0);
                    }
                    Client.state[Client.rn] = false;
                    pw.append(Client.frames[Client.rn] + " ");
                    pw.flush();
                    Client.rn++;
                    Client.rn %= 16;
                }
                /*Window Sliding Ends*/

                s = new Socket(Client.localhost,9999);
                dis = new DataInputStream(s.getInputStream());
                frame = dis.readUTF();
                s.close();

                String str = "";
                cur = 0;
                for(int i=0;i<8;i++){
                    cur *= 2;
                    cur += frame.charAt(i)-'0';
                    str += frame.charAt(i);
                }

                /*Random Error Generation for Acknowledgement*/
                Random rn = new Random();
                int rand = rn.nextInt() % 100;
                rand += 100;
                rand %= 100;
                
                TimerC tm = new TimerC(cur);
                tm.start();

                if(rand>80){
                    System.out.println("Packet " + cur + " is received but acknowledgement is not sent");
                    continue;
                }
                else System.out.println("Packet " + cur + " is received and acknowledgement is sent");
                /*Random Error Generation for Acknowledgement*/


                if( ( ( cur - Client.rn + 16 ) % 16 ) < 8){
                    cnt = 0;

                    //cur write korbo
                    Client.state[cur] = true;
                    String res = "";
                    for(int i=8;i<64+8;i+=8){
                        String tmp = "";
                        for(int j=i;j<i+8;j++){
                            tmp += frame.charAt(j);
                        }
                        int x = Integer.parseInt(tmp,2);
                        res += (char)x;
                    }
                    Client.frames[cur] = res;

                    s = new Socket(Client.localhost,8888);
                    dos = new DataOutputStream(s.getOutputStream());
                    rand = rn.nextInt() % 100;
                    rand += 100;
                    rand %= 100;

                    if(rand<80)dos.writeUTF(str+"1");
                    else{
                        System.out.println("Packet " + cur + " is received out of sequence");
                        dos.writeUTF(str+"0");
                    }
                    s.close();
                }
                else cnt++;

                if(cnt>10){
                    int x = 8,y = (Client.rn-1+16)%16;
                    while(x!=0){
                        rand = rn.nextInt() % 100;
                        rand += 100;
                        rand %= 100;

                        if(rand>80){
                            x--;

                            y--;
                            y += 16;
                            y %= 16;
                            continue;
                        }
                        else System.out.println("Acknowledgement for Packet " + cur + " is not sent");



                        String temp = Integer.toString(y, 2);
                        while(temp.length()<8) temp = 0 + temp;

                        s = new Socket(Client.localhost,8888);
                        dos = new DataOutputStream(s.getOutputStream());
                        dos.writeUTF(temp + "1");
                        s.close();
                        x--;

                        y--;
                        y += 16;
                        y %= 16;
                    }
                }

            }
        }catch(Exception e){
            try {
                Socket s = new Socket(Client.localhost,8888);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF("00");
                s.close();
                System.exit(0);
            } catch (IOException ex) {
                
            }
            
        }
    }
}

public class Client{
    public static String localhost = "172.16.13.211";
    public static int sw;
    public static int sz = 32;
    public static int rn = 0;
    public static boolean[] state = new boolean[100];
    public static String[] frames = new String[100];
    public static boolean[] timeOut = new boolean[100];
    public static void main(String[] args) throws IOException{
        try{
            for(int i=0;i<100;i++) state[i] = false;

            Reciever r = new Reciever();

            r.start();
        }catch(Exception e){

        }
    }
}