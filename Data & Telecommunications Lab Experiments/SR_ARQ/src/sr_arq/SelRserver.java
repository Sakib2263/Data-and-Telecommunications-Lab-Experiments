/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sr_arq;

/**
 *
 * @author Tanjid Hasan Tonmoy
 */

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import static sr_arq.SelRClient.var;
public class SelRserver {
    public static void main(String args[]){try {
            //Create Socket at Server Side
            ServerSocket s=new ServerSocket(95);
            Socket con =s.accept();
            System.out.println("Server Selective Repeat Started...");
            
            BufferedReader fromclient = new BufferedReader(new InputStreamReader(con.getInputStream()));
            DataOutputStream toclient= new DataOutputStream(con.getOutputStream());
            OutputStream output = con.getOutputStream();
            
            LinkedList list = new LinkedList();

            String f = fromclient.readLine();
            
            int nf=Integer.parseInt(f);
            //System.out.println("Number of packets = "+nf);
            
            int lost_ack=0,rndnum=1,lost=0;
            String rndm=fromclient.readLine();
            rndnum= Integer.parseInt(rndm);
            
            String flow = fromclient.readLine();
            //String flow = "n";
        
            
            if(flow.contains("n") || flow.contains("N")){
                for(int i=0;i<var;i++   ){             
                    if(i==rndnum) {
                        System.out.println("Not received "+i);
                        list.add(i,"lost");
                        lost=i;
                        continue;
                    }
                    else{
                        System.out.println("Received " + i);
                    }
                
                    String n=fromclient.readLine();
                
                    int num=Integer.parseInt(n);
                    String packet=fromclient.readLine();
                    list.add(num,packet);
                
                    if(list.contains("lost")){
                    
                        lost_ack= list.indexOf("lost");
                        toclient.writeBytes(lost_ack+"\n");
                        num=lost_ack;
                    
                
                        if(i== rndnum){
                             lost_ack= list.indexOf("lost");
                    System.out.println("Sending lost Ack"+lost_ack);
                    toclient.writeBytes(lost_ack+"\n");
                        }
                        else if(i>rndnum){
                            if((i+1)%SelRClient.sz == 0 && i>0) System.out.println("sending ack"+(i+1) + "\n");
                            toclient.flush();
                            toclient.writeBytes(i+"\n");
                            toclient.flush();
                        }else
                        {
                            if((i+1)%SelRClient.sz == 0 && i>0)System.out.println("sending ack"+(i+1)+ "\n");
                            toclient.flush();
                            toclient.writeBytes(num+"\n");
                            toclient.flush();
                        }
                    }else
                    {
                        if((i+1)%SelRClient.sz == 0 && i>0) System.out.println("sending ack"+ (i+1)+ "\n");
                        toclient.flush();
                        toclient.writeBytes(num+"\n");
                        toclient.flush();
                    }
               
                }
                //System.out.println(list);
                String n=fromclient.readLine();
                int num=Integer.parseInt(n);
                String packet=fromclient.readLine();
                System.out.println("received packet"+num+" = "+packet);
                list.set(lost,packet);
                //System.out.println(list);
                toclient.flush();
                int ack=num;
            
                //Send the acknowledgment for lost packet,once it is received from client again.
               
            }else{
                for(int i=0;i<var; i++){                  String n=fromclient.readLine();
                    int num=Integer.parseInt(n);
                    String packet=fromclient.readLine();
                    list.add(num,packet);
                    System.out.println("received packet"+num+" = "+packet);
                    System.out.println("sending ack"+i);
                    toclient.flush();
                    toclient.writeBytes(i+"\n");
                    toclient.flush();
                }
                System.out.println("received the following packets"+list);
            }
            //Close connections with client
            fromclient.close();
            output.close();
            con.close();
            s.close();
        }catch (Exception e) {
            System.out.println("\n\n ERROR"+e);
        }
    }
}
