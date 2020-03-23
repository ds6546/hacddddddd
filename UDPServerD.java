import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.List;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date; 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Set;

public class UDPServerD {
	
    private List<Client> network;
    DatagramSocket socket;
    
    public UDPServerD(List<Client> passed_net)
    {
        network = passed_net;
    }
	
    public void executeServer()
    {	
        try{
            socket = new DatagramSocket(1234);
            socket.setSoTimeout(300000);
            byte[] incomingData = new byte[1024];   
            
            while(true)
            {
                DatagramPacket receivePacket = new DatagramPacket(incomingData, incomingData.length);
                System.out.println("\n--------Server is listening ----------\n");
                
                Calendar wait_till = Calendar.getInstance();
                
                if(network.isEmpty())
                {
                    try{
                        socket.receive(receivePacket);
                        
                        String message = new String(receivePacket.getData());
                        InetAddress address = receivePacket.getAddress();
			int port = receivePacket.getPort();
                        System.out.println("Received message: " + message);
                        System.out.println("Client IP: "+ address.getHostAddress());
                        System.out.println("Client port: "+ port);
                        //Creating newClient to add to arraylist
                        Client newClient = new Client(address, port);
                        network.add(newClient);
                        
                        String response = "Thank you for the message ----- Response sent from server";
                        Packet sendPkt = new Packet(network, response);
                        
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        ObjectOutputStream os = new ObjectOutputStream(outputStream);
                        os.writeObject(sendPkt);
                        byte[] sendData = outputStream.toByteArray();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                        System.out.println("Sending packet");
                        socket.send(sendPacket);
                        System.out.println("Packet sent");   
                        
                        wait_till = Calendar.getInstance();
                        wait_till.add(Calendar.SECOND, 30);   //Maximum wait time until next message
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }  
                
                //Network is not empty
                else{
                    while((Calendar.getInstance().compareTo(wait_till) <= 0) && (!is_all_received()))
                    {
                        try{
                            socket.receive(receivePacket);
                            
                            String message = new String(receivePacket.getData());
                            InetAddress address = receivePacket.getAddress();
                            int port = receivePacket.getPort();
                            System.out.println("Received message: " + message);
                            System.out.println("Client IP: "+ address.getHostAddress());
                            System.out.println("Client port: "+ port);
                            
                            if(!isIPpresent(address))
                            {
                                network.add(new Client(address, port));
                            }
                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    String response = "Thank you for the message ----- Response sent from server";
                    Packet sendPkt = new Packet(network, response);    
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try{
                        ObjectOutputStream os = new ObjectOutputStream(outputStream);
                        os.writeObject(sendPkt);
                        byte[] sendData = outputStream.toByteArray();
                        sendPackets(sendData);
                        System.out.println("Packets sent");                         
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }      
                    
                    wait_till = Calendar.getInstance();
                    wait_till.add(Calendar.SECOND, 30);   //Maximum wait time until next message
                }
            }
        }
        
        catch(SocketException e)
        {
            e.printStackTrace();
        }
    }

    public boolean is_all_received()
    {
        for(int i = 0; i<network.size(); i++)
        {
            if (!network.get(i).getis_received())
            {
                return false;
            }
        }
        return true;
    }
    
    public boolean isIPpresent(InetAddress address)
    {
        for(int i = 0; i<network.size(); i++)
        {
            if(network.get(i).getIP().equals(address)){
                network.get(i).setReceivedTrue();
                return true;
            }
        }
        return false;
    }

    public void sendPackets(byte[] data)
    {
        for(int i = 0; i < network.size(); i++)
        {
            if(network.get(i).getis_received())
            {
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, network.get(i).getIP(), network.get(i).getPort());
                try{
                socket.send(sendPacket);
                }
                catch(IOException x)
                {
                    System.out.println("Can't send packets");
                }
            }
        }
        
    }

}
