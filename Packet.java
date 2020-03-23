
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maste
 */
public class Packet implements Serializable{
    private List<Client> network;
    private String msg;
    
    public Packet(List<Client> clients, String msg)
    {
        this.network = clients;
        this.msg = msg;
    }
    
    public List<Client> getClientArray()
    {
        return network;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    public void printClientArray()
    {
        for (int i=0; i<network.size(); i++)
        {
            network.get(i).printIP();
        }
    }
}
