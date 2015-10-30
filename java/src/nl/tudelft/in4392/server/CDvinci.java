package nl.tudelft.in4392.server;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.manager.JobManager;
import nl.tudelft.in4392.manager.VMmanager;
import nl.tudelft.in4392.model.Job;
import nl.tudelft.in4392.model.VinciVM;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ardhipoetra on 10/6/15.
 */
public class CDvinci extends UnicastRemoteObject implements _CDvinci {

    public HashMap<String, ArrayList<Job>> jobUserMap;

    public CDvinci() throws Exception{
        jobUserMap = new HashMap<String, ArrayList<Job>>();

        System.out.println("create vmlist");
        VMmanager.showAllVms();
        VMmanager.startVMmonitor();

        JobManager.checkRunJob();
    }

    @Override
    public Job addJob(String userID, final Job j) throws RemoteException {
        j.status = Job.JOB_WAITING;

        ArrayList<Job> jarlist;

        if(jobUserMap.get(userID) == null) {
            jarlist = new ArrayList<Job>();
            jobUserMap.put(userID, jarlist);
        } else
            jarlist = jobUserMap.get(userID);

        jarlist.add(j);

        Thread t = new Thread() {
            @Override
            public void run() {
                JobManager.submitJobtoVM(j);
            }
        };

        t.start();



        System.out.printf("Retrieved job %s from %s\n", j, userID);

        return j;
    }

    @Override
    public int getTotalJobs(String userID) throws RemoteException {

        if (userID.equals(Constants.TEST_ADMIN)) {

            int totalJobs = 0;
            for(String ukey : jobUserMap.keySet()) {
                totalJobs += getTotalJobs(ukey);
            }
            return totalJobs;
        }

        return jobUserMap.get(userID) == null ? 0 : jobUserMap.get(userID).size();
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

            for(String ukey : jobUserMap.keySet()) {
                System.out.println("uid "+ukey);
                printJobs(ukey);
            }
            return ;
        }

        Iterator<Job> iterator = this.jobUserMap.get(userID).iterator();

        while(iterator.hasNext()) {
            Job js = iterator.next();
			System.out.println(js);
        }

    }

    @Override
    public int call(String userID, String command) throws RemoteException {

        String returnCallResult = "";
        try {
            if(command.equalsIgnoreCase("showvms")) {
                System.out.println("show vms");
                returnCallResult = VMmanager.showAllVms();
            } else if(command.startsWith("checkmem")) {
                String id = command.split(" ")[1];

                System.out.println("check mem id : "+id);
                VinciVM vm = VMmanager.getVM(Integer.parseInt(id));

                Utility.callSSH(vm.hostname, vm.checkMemComm());

                returnCallResult = "success";

            } else if(command.startsWith("checkcpu")) {
                String id = command.split(" ")[1];

                System.out.println("check cpu id : "+id);
                VinciVM vm = VMmanager.getVM(Integer.parseInt(id));

                Utility.callSSH(vm.hostname, vm.checkCpuComm());

                returnCallResult = "success";

            }else {
                if (command.equals("3")) returnCallResult = VMmanager.showVMInfo(VMmanager.getVM(40928));
                else if (command.equals("4")) VMmanager.deleteVM(VMmanager.getVM(40928));
                else if (command.equals("5")) VMmanager.createVM();


                System.out.println("call default SSH command");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (returnCallResult.equals(""))
            Utility.callSSH(Constants.TEST_TARGET_SSH, command);
        else
            System.out.println("out : \n"+returnCallResult);
        return 0;
    }
}
