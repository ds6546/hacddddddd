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

public class UDPServerD {
	
    private List<Client> network;	
    public UDPServerD(List<Client> passed_net)
    {
        network = passed_net;
    }
	
    public void executeServer()
    {	
        try{
            DatagramSocket socket = new DatagramSocket(1234);
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
                        System.out.println("Received message: " + message);
                        InetAddress address = receivePacket.getAddress();
			int port = receivePacket.getPort();
                        System.out.println("Client IP: "+ address.getHostAddress());
                        System.out.println("Client port: "+ port);
                        //Creating newClient to add to arraylist
                        Client newClient = new Client(address, true);
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
                    System.out.println("Network has a Client");
                }
            }
        }
        
        catch(SocketException e)
        {
            e.printStackTrace();
        }
    }




}
