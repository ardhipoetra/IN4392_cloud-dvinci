package nl.tudelft.in4392.manager;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.model.Job;
import org.opennebula.client.vm.VirtualMachine;

import javax.rmi.CORBA.Util;

/**
 * Created by ardhipoetra on 10/9/15.
 */
public class JobManager {

    public static int submitJobtoVM(Job jj) {

        VirtualMachine vmTarget = VMmanager.getAvailableVM();

        // find IP/hostname of vmTarget
        String hname = Constants.TEST_TARGET_SSH;

        Utility.callSSH(hname, "echo \"`hostname` "+jj.id+" "+jj.tasks.get(0).action+"\"");
        return 0;
    }
}
