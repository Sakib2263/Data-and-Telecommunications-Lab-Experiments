
package sr_arq;

/**
 *
 * @author Tanjid Hasan Tonmoy
 */
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SelRClient {
    static int var = 15;
    static int sz = 7;
    
@SuppressWarnings("static-access")
    public static void main(String args[]) {
    
    long start = System.currentTimeMillis();
    try
    {
        //Create Socket at client side
        Socket SRClient =new Socket("localhost",95);
        
        //Receive the input from console
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream toSrServer= new DataOutputStream(SRClient.getOutputStream());
        BufferedReader fromSrServer = new BufferedReader(new InputStreamReader(SRClient.getInputStream()));
        
        //Create linked list to display and store the packets 
        LinkedList list = new LinkedList();
        
        //System.out.println("We maintained window size as 4,so for better performance evaluation kindly enter atleast 15 packets in input \n");
        
        Random rndNumbers = new Random();
        int rndNumber = 0;
        
        //System.out.println("Enter number of packets to be transmitted");
        
        String n= String.valueOf(var);
        int nf=Integer.parseInt(n);
        toSrServer.writeBytes(nf+"\n");
        int temp=nf-1;
        
        //Generate a random number so that particular packet is lost in demo
        //rndNumber = rndNumbers.nextInt();
        rndNumber = ThreadLocalRandom.current().nextInt(sz, temp);
        
        for(int i=0;i<var; i++           ){ 
            //System.out.println("Enter packet"+i+"to be transmitted");
            String packet= "" + i;
            list.add(packet);
        }
        System.out.println("\nYou have entered following data "+list+"\n");
        
        toSrServer.writeBytes(rndNumber+"\n");
        
        //System.out.println("Please enter Y for  successful sending or N for packet loss");
        //String flow = in.readLine();
        String flow = "N";
        
        toSrServer.writeBytes(flow+"\n");

         int w=sz,wind=0,p=0,q=0;
         
        //While sending packet ignore the random number generated packet.
         if(flow.contains("n") || flow.contains("N")){
             for(int i=0;i<var; i++){      p=i;
                 wind = windowSize(p,q);
            
                if((i==rndNumber)){
                    
                    System.out.println("Negative ack received "+i);
                    q=w;
                    wind = windowSize(p,q);
                    wind++;
                    q=wind;
                    
                     //Re-send the lost packet
            System.out.println("Resending packet"+rndNumber);
            String str=(String) list.get(rndNumber);
        
            toSrServer.writeBytes(rndNumber+"\n");
            toSrServer.writeBytes(str+"\n");
            toSrServer.flush();
            
                    //repeatack(rndNumber);
            
                    continue;
                } else {
                    String str=(String) list.get(i);
                    System.out.println("Sending " + str);
                    toSrServer.writeBytes(i+"\n");
                    toSrServer.writeBytes(str+"\n");
                }

                wind++;
                q=wind;
                
                String ak = fromSrServer.readLine();
                int ack = Integer.parseInt(ak);
                ack=i+1;
                if((i+1)%sz == 0 && i>0) System.out.println("Ack received for "+ack);
            }
        
        //Timer set for expected acknowledgment 
            Thread.currentThread().sleep(2000);

            String as = fromSrServer.readLine();
        
            System.out.println("Recieved ack no"+rndNumber);
            int aa = Integer.parseInt(as);
            if (aa != (list.size() -1))
                System.out.println("Ack received for "+rndNumber);
         }else
         {
             for(int i=0;i<var; i++){      p=i;
                 wind = windowSize(p,q);
                String str=(String) list.get(i);
                 toSrServer.writeBytes(i+"\n");
                 toSrServer.writeBytes(str+"\n");
                 wind++;
                q=wind;
                 String ak = fromSrServer.readLine();
                int ack = Integer.parseInt(ak);
                ack=i;
                System.out.println("Ack received for "+ack);
             }
         }
        //Close socket and server connections
        fromSrServer.close();
        toSrServer.close();
        SRClient.close();
    }catch (Exception e){
        System.out.println("\n\n ERROR"+e);
    }
    
    //Calculate performance
    long end = System.currentTimeMillis();
    NumberFormat formatter = new DecimalFormat("#0.00000");
    System.out.println("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");

    }
    public static int windowSize(int p,int wind) throws InterruptedException
    {
        int i=p,w=i+sz,windw=wind;
        LinkedList < Integer > list1 = new LinkedList < Integer > ();
        if(i==0 || windw==sz){
            System.out.println("\nCurrent window");
            for(int q=i;q <i+sz;q++         ){      windw=0;
                list1.add(q);
            
            }
            System.out.println(list1);
            if(windw==0){
                Thread.currentThread();
                Thread.sleep(2000);
            }
        }
        return windw;
    }
    
    static void repeatack(int rndNumber){
        System.out.println("Recieved ack no"+rndNumber);
    }
}