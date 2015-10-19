package nl.tudelft.in4392.server;

import cabbott.net.SSHManager;
import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.manager.JobManager;
import nl.tudelft.in4392.manager.VMmanager;
import nl.tudelft.in4392.model.Job;
import org.opennebula.client.vm.VirtualMachine;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ardhipoetra on 10/6/15.
 */
public class CDvinci extends UnicastRemoteObject implements _CDvinci {

    public HashMap<String, ArrayList<Job>> jobList;

    public CDvinci() throws Exception{
        jobList = new HashMap<String, ArrayList<Job>>();
    }

    @Override
    public Job addJob(String userID, Job j) throws RemoteException {
        j.status = Job.JOB_SUBMITTED;

        ArrayList<Job> jarlist;

        if(jobList.get(userID) == null) {
            jarlist = new ArrayList<Job>();
            jobList.put(userID, jarlist);
        } else
            jarlist = jobList.get(userID);

        jarlist.add(j);

        JobManager.submitJobtoVM(j);

        System.out.printf("Retrieved job %s from %s\n", j, userID);

        return j;
    }

    @Override
    public int getTotalJobs(String userID) throws RemoteException {

        if (userID.equals(Constants.TEST_ADMIN)) {

            int totalJobs = 0;
            for(String ukey : jobList.keySet()) {
                totalJobs += getTotalJobs(ukey);
            }
            return totalJobs;
        }

        return jobList.get(userID) == null ? 0 : jobList.get(userID).size();
    }


	@Override
	public void printJobs(String userID) throws RemoteException {

        int size = getTotalJobs(userID);

        if (size == 0) {
            System.out.println("Empty job");
            return ;
        }

		System.out.println("printing jobs : " + size);

        if (userID.equals(Constants.TEST_ADMIN)) {

            for(String ukey : jobList.keySet()) {
                System.out.println("uid "+ukey);
                printJobs(ukey);
            }
            return ;
        }

        Iterator<Job> iterator = this.jobList.get(userID).iterator();

        while(iterator.hasNext()) {
            Job js = iterator.next();
			System.out.println(js);
        }

    }

    @Override
    public int call(String userID, String command) throws RemoteException {
        Utility.callSSH(Constants.TEST_TARGET_SSH, command);
        return 0;
    }
}
