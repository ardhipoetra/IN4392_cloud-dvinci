package nl.tudelft.in4392.manager;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.model.Job;
import nl.tudelft.in4392.model.VinciVM;
import org.opennebula.client.vm.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ardhipoetra on 10/9/15.
 */
public class JobManager {


    static CopyOnWriteArrayList<Job> runningJobsList = new CopyOnWriteArrayList<Job>();

    static Logger logger = LoggerFactory.getLogger(JobManager.class);

    public static int submitJobtoVM(final Job jj) {

        final VinciVM vmTarget = VMmanager.getAvailableVM(); // may block here

        // find IP/hostname of vmTarget
        final String hname = vmTarget.hostname; //Constants.TEST_TARGET_SSH;

        System.out.println("\t\t\t\t\t\tshoot to "+vmTarget.id()+"\n");

        jj.status = Job.JOB_RUNNING;

        runningJobsList.add(jj);

        // start start
        jj.idVmTarget = vmTarget.id();

        vmTarget.runningJobs++;

        Utility.callSSH(hname, "cd "+Constants.START_WORKSPACE_VM+"; "+jj.getCommands()); // and here
        jj.status = Job.JOB_FINISH;

        runningJobsList.remove(jj);

        vmTarget.runningJobs--;

        JobManager.informJobFinish(jj);
        // start end

        // return success for now
        return jj.status;
    }


    public static void informJobFinish(final Job jj) {
        jj.timeFinish = System.currentTimeMillis();

        System.out.println("Job "+jj+" has finished in "+(jj.timeFinish - jj.timeStart));
    }

    public static void checkRunJob() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while(true) {

                    Iterator<Job> runit = runningJobsList.iterator();

                    int[] totalUserJob = {0,0};

                    System.out.print(">>>> ");
                    while (runit.hasNext()) {
                        Job jj = runit.next();
                        System.out.print(jj.id + " ");

                        if(System.currentTimeMillis() - jj.timeStart > 1000 * 1000 ||
                                jj.status == Job.JOB_ERROR) { // 1000 second timeout

//                            submitJobtoVM(jj);
                        }

                        if (jj.uid == Constants.TEST_USER_1) {
                            totalUserJob[0]++;
                        } else {
                            totalUserJob[1]++;
                        }

                    }
                    logger.info("TotalJob 1: %d 2: %d", totalUserJob[0], totalUserJob[1]);
                    System.out.println(" <<<");



                    try {
                        Thread.sleep(Constants.VM_MONITOR_CHECK_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

//        t.start();
    }
}
