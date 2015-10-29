package nl.tudelft.in4392;
import nl.tudelft.in4392.manager.VMmanager;
import nl.tudelft.in4392.server.CDvinci;
import nl.tudelft.in4392.server._CDvinci;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class Main {

    static {
        // set a system property such that Simple Logger will include timestamp
        System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
        // set a system property such that Simple Logger will include timestamp in the given format
//        System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "dd-MM-yy HH:mm:ss");
        // configure SLF4J Simple Logger to redirect output to a file
        System.setProperty("org.slf4j.simpleLogger.logFile", "dvincilog.log");

        System.setProperty("org.slf4j.simpleLogger.showThreadName", "false");
    }

    public static void main(String[] args) throws Exception {
        System.setSecurityManager(new RMISecurityManager());

        System.setProperty("java.security.policy", "file:///home/cld1593/cloud-dvinci/java/security.policy");
        _CDvinci srv = new CDvinci();

        Naming.rebind("rmi://localhost/RDsrv", srv);
//        Naming.rebind("rmi://localhost/RDfac", ((CDvinci)srv).factory);

        System.out.println("ready.");
    }
}