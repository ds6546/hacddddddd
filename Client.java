
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Calendar;

public class Client implements Serializable{
	private InetAddress IPAddress;
	boolean is_received;
        int port;
	
    public Client(InetAddress IP, int port)

    {

    	this.IPAddress = IP;
        this.port = port;
        this.is_received = true;

    }
    
    public InetAddress getIP()

    {

    	return IPAddress;

    }
    
    public int getPort(){
        return port;
    }
    
    public boolean getis_received()
    {
        return is_received;
    }
    
    public void printIP() {
        System.out.println(IPAddress.getHostAddress());
    }
    
    public void setReceivedTrue()
    {
        is_received = true;
    }
    
    public void setReceivedFalse()
    {
        is_received = false;
    }
}
