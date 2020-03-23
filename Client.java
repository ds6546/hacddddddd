
import java.net.InetAddress;
import java.util.Calendar;

public class Client {
	private InetAddress IPAddress;
	boolean is_received;
	
    public Client(InetAddress IP, boolean was_received)

    {

    	this.IPAddress = IP;

        this.is_received = was_received;

    }
    
    public InetAddress getIP()

    {

    	return IPAddress;

    }
    
    public boolean getis_received()
    {
        return is_received;
    }
    
    public void printIP() {
        System.out.println(IPAddress.getHostAddress());
    }
}
