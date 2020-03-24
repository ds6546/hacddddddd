import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;
import java.net.UnknownHostException;

/**
 * @author Team3
 *
 */
public class HAC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Are you a client or a server? \n"
                + "Enter \"C\" if you are a client and \"S\" if you are a server");
        Scanner keyboard = new Scanner(System.in);
        char input = keyboard.next().charAt(0);
        
        if(input == 'C')
        {   List<Client> nettoserver = new ArrayList<>();
            System.out.println("Hey client, what is the server IP address you want to connect to?");
            Scanner k2 = new Scanner(System.in);
            String server_address = k2.nextLine();
            try{ 
            	InetAddress add = InetAddress.getByName(server_address); 
            	UDPClientD c = new UDPClientD(add);
            	nettoserver = c.executeClient();
            }
            catch(UnknownHostException e)
            {
                System.out.println("Server not found!");
            }
            UDPServerD theServer = new UDPServerD();
            theServer.executeServer();
        }
        
        else if(input == 'S')
        {
            System.out.println("I am the server, I am receiving the data");
            UDPServerD theServer = new UDPServerD();
            theServer.executeServer();
        }
        keyboard.close();


	}

}
