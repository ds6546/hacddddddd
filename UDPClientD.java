
import java.io.ByteArrayInputStream;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.net.*;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UDPClientD
{
    private List<Client> network;
    private InetAddress server_address;
    DatagramSocket socket;
    DatagramPacket incomingPacket;
        
    public UDPClientD(InetAddress add, List<Client> passed_net)
    {
        server_address = add;
        network = passed_net;
    }
        
    public void setServerAddress(InetAddress add)
    {
        server_address = add;
    }
    
    public void executeClient() 
    {
        Random rand = new Random();
        
        boolean hasConnectionEstablished = false;
        while(true)
        {
            int random_int = 0;
            if (hasConnectionEstablished == true)
            {
                random_int = rand.nextInt(30000);
            }
            
            try 
            {
                Thread.sleep(random_int);
                
                try{
                    socket = new DatagramSocket();
                    InetAddress address = server_address;
                    String message = "Hello! from the Client";
                    byte[] sendMessage = message.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendMessage, sendMessage.length, address, 1234);
                    socket.send(sendPacket);
                    System.out.println("Message sent");
                    
                    Calendar wait_from_server_till = Calendar.getInstance();
                    wait_from_server_till.add(Calendar.SECOND,30);
                    
                    byte[] incomingData = new byte[1024];
                    incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    
                    socket.setSoTimeout(30000);
                    if(receiveUnntilTimeout()==0)
                    {
                        return;
                    }
                    
                    hasConnectionEstablished = true;
                    byte[] data = incomingPacket.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(data);
                    ObjectInputStream is = new ObjectInputStream(in);
                    try {
                        Packet pkt = (Packet)is.readObject();
                        System.out.println("Message received from Server = " + pkt.getMsg());
                        
                        if(pkt.getMsg().equals("I am your new server!")) {
                            setServerAddress(incomingPacket.getAddress());
                            System.out.println("Server changed! The new server is: " + incomingPacket.getAddress().getHostAddress());
                            hasConnectionEstablished = false;
                            continue;
                        }
                        
                        pkt.remove_nonresponding_Clients();
                        System.out.println("Active clients: -----");
                        pkt.printClientArray();
                        
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                catch(SocketException z)
                {
                    z.printStackTrace();
                }   
                catch(IOException i)
                {
                    i.printStackTrace();
                }
                
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public int receiveUnntilTimeout()
    {
        System.out.println("waiting");
        try{
            socket.receive(incomingPacket);
            System.out.println("received");
        }
        catch (SocketTimeoutException e){
           socket.close();
           remove_myself();
           System.out.println("I am the server now");
           return 0;
        } catch (IOException ex) {
            Logger.getLogger(UDPServerD.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return 1;
    }
    
    public void remove_myself()
    {
        int i = 0;
        boolean has_found = false;
        while(!has_found)
        {
            try {
                if(network.get(i).getIP().equals(InetAddress.getLocalHost()))
                {
                    has_found = true;
                    network.remove(i);    
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(UDPClientD.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
    }
    
}
        
        
    
