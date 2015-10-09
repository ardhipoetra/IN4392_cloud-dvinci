package nl.tudelft.in4392;

import cabbott.net.SSHManager;

/**
 * Created by ardhipoetra on 10/9/15.
 */
public class Utility {
    public static int callSSH(String target, String command) {
        SSHManager instance = new SSHManager(target, Constants.SSH_KNOWN_HOSTS);
        String errorMessage = instance.connect();

        if(errorMessage != null)
        {
            System.out.println(errorMessage);
            return -1;
        }

        String result = instance.sendCommand(command);
        instance.close();

        System.out.println("res : "+result);
        return 0;
    }

}
