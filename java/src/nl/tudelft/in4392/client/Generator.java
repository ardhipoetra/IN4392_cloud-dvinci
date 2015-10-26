package nl.tudelft.in4392.client;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.model.Job;
import nl.tudelft.in4392.model.Task;
import nl.tudelft.in4392.server._CDvinci;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ardhipoetra on 10/6/15.
 */
public class Generator {

    static Timer genJobs;
    static int numJobsRun;


    public static void main(String[] args) throws Exception{
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        final _CDvinci cdisrv = (_CDvinci) Naming.lookup("rmi://localhost/RDsrv");

        boolean running = true;
        Scanner s = new Scanner(System.in);

        while(running){
            System.out.println("0 : exit\n1:gettotal jobs\n2:list jobs\nother number : send jobs");
            System.out.print("Enter command: ");
            String command = s.next();


            if(command.equals("0"))
                running = false;
            else if(command.equals("1")) {
                System.out.println(cdisrv.getTotalJobs(Constants.TEST_USER_1));
            } else if(command.equals("2")) {
                cdisrv.printJobs(Constants.TEST_USER_1);
            }

            else {
                final int numJobs = Integer.parseInt(command);

                genJobs = new Timer(true);

                genJobs.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (numJobsRun++ == numJobs) {
                            genJobs.cancel(); genJobs.purge();
                        }

                        Job jj = new Job("name"+numJobsRun, "URI:" + numJobsRun);
                        try {
                            jj.id = new Random().nextInt() + numJobsRun;
                            jj.addTask(new Task(Task.TASK_ROTATE+"-"+numJobsRun));
                            jj.addTask(new Task(Task.TASK_RESIZE+"-"+numJobsRun));

                            System.out.println("job id "+jj.id+" created");

                            cdisrv.addJob(Constants.TEST_USER_1,jj);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, Constants.INTERVAL_JOB);
            }

            System.out.println(command);
        }
    }
}