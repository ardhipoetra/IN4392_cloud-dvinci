package nl.tudelft.in4392.manager;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.model.Job;
import nl.tudelft.in4392.model.VinciVM;
import org.opennebula.client.vm.VirtualMachine;

/**
 * Created by ardhipoetra on 10/9/15.
 */
public class JobManager {

    public static int submitJobtoVM(final Job jj) {

        VinciVM vmTarget = VMmanager.getAvailableVM();

        // find IP/hostname of vmTarget
        final String hname = vmTarget.hostname; //Constants.TEST_TARGET_SSH;

        Thread t = new Thread() {
            @Override
            public void run() {

                Utility.callSSH(hname, "cd "+Constants.START_WORKSPACE_VM+"; "+jj.getCommands());
                jj.status = Job.JOB_FINISH;

                JobManager.informJobFinish(jj);
            }
        };

        jj.status = Job.JOB_RUNNING;
        t.start();

        // return success for now
        return jj.status;
    }


    public static void informJobFinish(final Job jj) {

    }
}
