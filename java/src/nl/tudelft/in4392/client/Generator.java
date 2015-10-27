package nl.tudelft.in4392.client;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.model.Job;
import nl.tudelft.in4392.model.Task;
import nl.tudelft.in4392.server._CDvinci;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.*;

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

                        Iterator<File> it = FileUtils.iterateFiles(new File("/home/cld1593/stockpic/"), null, false);

                        while(it.hasNext()) {
                            String fname = "";
                            File f = it.next();
                            try {
                                fname = f.getCanonicalPath();
                            } catch (Exception e) {fname = f.getAbsolutePath();}


                            Job jj = new Job("name"+numJobsRun, fname, md5user);
                            jj.setDestPath(fname + "_" + md5user + "/" + numJobsRun);
                            try {

                                jj.id = new Random().nextInt() + numJobsRun;

                                Task t = new Task(Task.TASK_ROTATE);
                                t.addParam("90");

                                jj.addTask(t);
                                jj.addTask(t);
                                jj.addTask(t);

                                System.out.println("job id "+jj.id+" created");

                                cdisrv.addJob(md5user,jj);

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            numJobsRun++;
                        }
                        genJobs.cancel();
                        genJobs.purge();
                    }
                }, 0, Constants.INTERVAL_JOB);
            }

            System.out.println(command);
        }
    }
}