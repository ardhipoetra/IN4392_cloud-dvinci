package nl.tudelft.in4392.manager;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.model.Job;
import nl.tudelft.in4392.model.VinciVM;
import org.opennebula.client.vm.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ardhipoetra on 10/9/15.
 */
public class JobManager {

    static Logger logger = LoggerFactory.getLogger(JobManager.class);

    public static int submitJobtoVM(final Job jj) {

        final VinciVM vmTarget = VMmanager.getAvailableVM();

        // find IP/hostname of vmTarget
        final String hname = vmTarget.hostname; //Constants.TEST_TARGET_SSH;

        System.out.println("\t\t\t\t\t\tshoot to "+vmTarget.id()+"\n");


        Thread t = new Thread() {
            @Override
            public void run() {

                jj.idVmTarget = vmTarget.id();

                vmTarget.runningJobs++;

                Utility.callSSH(hname, "cd "+Constants.START_WORKSPACE_VM+"; "+jj.getCommands());
                jj.status = Job.JOB_FINISH;

                vmTarget.runningJobs--;

                JobManager.informJobFinish(jj);
            }
        };

        jj.status = Job.JOB_RUNNING;
        t.start();

        // return success for now
        return jj.status;
    }


    public static void informJobFinish(final Job jj) {
        jj.timeFinish = System.currentTimeMillis();

        System.out.println("Job "+jj+" has finished in "+(jj.timeFinish - jj.timeStart));
    }
}
