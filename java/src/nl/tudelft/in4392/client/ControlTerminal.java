package nl.tudelft.in4392.client;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.server._CDvinci;

import java.rmi.Naming;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ardhipoetra on 10/6/15.
 */
public class ControlTerminal {
    public static void main(String[] args) throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        _CDvinci cdisrv = (_CDvinci) Naming.lookup("rmi://localhost/RDsrv");

        boolean running = true;
        Scanner s = new Scanner(System.in);
        while (running) {
            System.out.print("Enter command: ");
            String command = s.nextLine();

            if (command.equals("0")) running = false;
            else if (command.equals("1")) System.out.println(cdisrv.getTotalJobs(Constants.TEST_ADMIN));
            else if (command.equals("2")) cdisrv.printJobs(Constants.TEST_ADMIN);
            else
                cdisrv.call(Constants.TEST_ADMIN, command);
        }
    }
}