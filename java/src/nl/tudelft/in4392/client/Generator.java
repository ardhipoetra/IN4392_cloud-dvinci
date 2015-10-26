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
    static String md5user;

    public static void main(String[] args) throws Exception{
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        int uid = 1;

        if (args.length == 1) uid = Integer.parseInt(args[0]);

        md5user = Constants.TEST_USER_1;
        switch (uid) {
            case 1:
                break;
            case 2:
                md5user = Constants.TEST_USER_2;
                break;
            default:

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
                System.out.println(cdisrv.getTotalJobs(md5user));
            } else if(command.equals("2")) {
                cdisrv.printJobs(md5user);
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

                        Job jj = new Job("name"+numJobsRun, "/home/cld1593/cloud-dvinci/java/out/img.jpg", md5user);
                        jj.setDestPath("/home/cld1593/cloud-dvinci/java/out/img"+numJobsRun+".jpg");
                        try {
                            jj.id = new Random().nextInt() + numJobsRun;

                            Task t = new Task(Task.TASK_ROTATE);
                            t.addParam("90");

                            jj.addTask(t);
                            System.out.println("job id "+jj.id+" created");
                            cdisrv.addJob(md5user,jj);
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