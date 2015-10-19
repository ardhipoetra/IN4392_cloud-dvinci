package nl.tudelft.in4392;
import nl.tudelft.in4392.server.CDvinci;
import nl.tudelft.in4392.server._CDvinci;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setSecurityManager(new RMISecurityManager());

        System.setProperty("java.security.policy", "file:///home/cld1593/cloud-dvinci/java/security.policy");
        _CDvinci srv = new CDvinci();

        Naming.rebind("rmi://localhost/RDsrv", srv);
//        Naming.rebind("rmi://localhost/RDfac", ((CDvinci)srv).factory);

        System.out.println("ready.");
    }
}