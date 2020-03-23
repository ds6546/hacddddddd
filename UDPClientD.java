
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


public class UDPClientD
{
    private List<Client> network;
    private InetAddress server_address;
    DatagramSocket socket;
        
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
                    String message = "Hello! from Client Windows, I am the simple guy";
                    byte[] sendMessage = message.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendMessage, sendMessage.length, address, 1234);
                    socket.send(sendPacket);
                    System.out.println("Message sent");
                    
                    byte[] incomingData = new byte[1024];
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    socket.receive(incomingPacket);
                    hasConnectionEstablished = true;
                    byte[] data = incomingPacket.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(data);
                    ObjectInputStream is = new ObjectInputStream(in);
                    try {
                        Packet pkt = (Packet)is.readObject();
                        System.out.println("Message received from Server = " + pkt.getMsg());
                        System.out.println("Client list received: -----");
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
    
}
        
        
    
