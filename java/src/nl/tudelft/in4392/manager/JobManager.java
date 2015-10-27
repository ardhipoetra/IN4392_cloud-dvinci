package nl.tudelft.in4392.manager;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.model.Job;
import org.opennebula.client.vm.VirtualMachine;

/**
 * Created by ardhipoetra on 10/9/15.
 */
public class JobManager {

    public static int submitJobtoVM(Job jj) {

        VirtualMachine vmTarget = VMmanager.getAvailableVM();

        // find IP/hostname of vmTarget
        String hname = Constants.TEST_TARGET_SSH;

        Utility.callSSH(hname, "cd /home/cld1593/cloud-dvinci/java/out; "+jj.getCommands());

        // return success for now
        return 0;
    }
}
