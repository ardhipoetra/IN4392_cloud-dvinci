package nl.tudelft.in4392;
import nl.tudelft.in4392.server.CDvinci;
import nl.tudelft.in4392.server.CDvinci_I;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setSecurityManager(new RMISecurityManager());

        System.setProperty("java.security.policy", "file:///home/cld1593/cloud-dvinci/java/security.policy");
        CDvinci_I srv = new CDvinci();

        Registry registry = LocateRegistry.getRegistry("localhost");
        registry.rebind("srv", srv);
//        Naming.rebind("rmi://localhost/RDsrv", srv);
        System.out.println("ready.");
    }
}