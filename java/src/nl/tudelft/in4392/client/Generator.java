package nl.tudelft.in4392.client;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.model.Job;
import nl.tudelft.in4392.model.Task;
import nl.tudelft.in4392.server._CDvinci;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

/*-------key variables-------
UID - user ID - args[0]
OID - operation ID - args[1]
source - source to file / folder - args[2]
destination - destination to file / folder - args[3]
parameters - args [4] - args[xx]
*/

/*-------operation id-------
0 - Exit
1 - Add jobs
2 - List jobs
3 - Get total number of jobs
*/

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

        int uid = Integer.parseInt(args[0]);

        md5user = Constants.TEST_USER_1;
        switch (uid) {
            case 2:
                md5user = Constants.TEST_USER_2;
                break;
            default:
        }
        final _CDvinci cdisrv = (_CDvinci) Naming.lookup("rmi://localhost/RDsrv");

        boolean running = true;
        Scanner s = new Scanner(System.in);

        int OID = Integer.parseInt(args[1]);
        switch (OID) {
            case 0:
                return;
            case 1:
                System.out.println("Creating a task... ");
                break;
            case 2:
                System.out.println(cdisrv.getTotalJobs(md5user));
                return;
            case 3:
                cdisrv.printJobs(md5user);
                return;
            default:
                System.out.println("Wrong command. Try executing one more time.");
                return;
        }

        File source = new File(args[2]);
        File destination = new File(args[3]);
        ArrayList<String> Multidest = new ArrayList<String>();
        int i = 0;

        if (!source.exists()) {
            System.out.println("The source path or file does not exist");
            return;
        }


        // we don't use directoryarg if not exist
        if (!destination.exists()) {
            System.out.println("The destination path or file does not exist. Creating a new one.");
            String fdate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            new File(source.getCanonicalFile().getParent()+"/"+md5user+"_"+fdate).mkdir();
        }

        if (source.isDirectory()) {
            File[] files = new File(source.getCanonicalPath()).listFiles();

            for(File file : files){
                if(file.isFile())
                    Multidest.add(file.getCanonicalPath());
            }
        } else if (source.isFile()) {
            Multidest.add(source.getCanonicalPath());
        } else {
            System.out.println("Source path/file error");
        }

        loopParseCommand(args,Multidest, source, cdisrv);
//        while(running){
//            System.out.println("0 : exit\n1:gettotal jobs\n2:list jobs\nother number : send jobs");
//            System.out.print("Enter command: ");
//            String command = s.next();
//
//
//            if(command.equals("0"))
//                running = false;
//            else if(command.equals("1")) {
//                System.out.println(cdisrv.getTotalJobs(md5user));
//            } else if(command.equals("2")) {
//                cdisrv.printJobs(md5user);
//            }
//
//            else {
//                final int numJobs = Integer.parseInt(command);
//
//                genJobs = new Timer(true);
//
//                genJobs.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//
//                        Iterator<File> it = FileUtils.iterateFiles(new File("/home/cld1593/stock2/"), null, false);
//
//                        while(it.hasNext()) {
//                            String fname = "";
//                            File f = it.next();
//                            try {
//                                fname = f.getCanonicalPath();
//                            } catch (Exception e) {fname = f.getAbsolutePath();}
//
//
//                            Job jj = new Job("name"+numJobsRun, fname, md5user);
//                            jj.setDestPath("/home/cld1593/stock2/user");
//                            try {
//
//                                jj.id = new Random().nextInt() + numJobsRun;
//
//                                Task trot = new Task(Task.TASK_ROTATE);
//                                trot.addParam("90");
//                                jj.addTask(trot);
//
//                                Task tblur = new Task(Task.TASK_BLUR);
//                                tblur.addParam("0");
//                                tblur.addParam("8");
//                                jj.addTask(tblur);
//
//                                Task tblur2 = new Task(Task.TASK_SHARPEN);
//                                tblur2.addParam("4");
//                                tblur2.addParam("8");
//                                jj.addTask(tblur2);
//
//                                System.out.println("job id "+jj.id+" created");
//                                cdisrv.addJob(md5user,jj);
//
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                            numJobsRun++;
//                        }
//
//                        if(numJobsRun == numJobs) {
//                            genJobs.cancel();
//                            genJobs.purge();
//                        }
////                        genJobs.cancel();
////                        genJobs.purge();
//                    }
//                }, 0, Constants.INTERVAL_JOB);
//            }
//
//            System.out.println(command);
//        }
    }

    private static void loopParseCommand(String[] args, ArrayList<String> Multidest,
                                         File source, _CDvinci cdisrv) throws Exception{
        for (String m1 : Multidest) {
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            int unique = new Random().nextInt();
            String jobname = date + "_" + md5user + "_" + Integer.toString(unique);

            Job jj = new Job(jobname, m1, md5user);
            jj.setDestPath(new File(source.getCanonicalFile().getParent()+"/"+md5user+"_"+date).getCanonicalPath());
            jj.id = unique;

            Task tmpTask = null;
            int j = 5;

            for (String arg : args) {
                if (arg.equals("-convert")) {
                    tmpTask = new Task(Task.TASK_CONVERT);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.equals("-scaletox")) {
                    tmpTask = new Task(Task.TASK_SCALEX);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.equals("-scaletoy")) {
                    tmpTask = new Task(Task.TASK_SCALEY);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.equals("-resize")) {
                    tmpTask = new Task(Task.TASK_RESIZE);
                    tmpTask.addParam(args[j]);
                    tmpTask.addParam(args[j+1]);
                    j+=3;
                }
                else if (arg.equals("-name")) {
                    tmpTask = new Task(Task.TASK_NAME);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.equals("-rotate")) {
                    tmpTask = new Task(Task.TASK_ROTATE);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.equals("-colorspace")) {
                    tmpTask = new Task(Task.TASK_COLORSPACE);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.equals("-sharpen")) {
                    tmpTask = new Task(Task.TASK_SHARPEN);
                    tmpTask.addParam(args[j]);
                    tmpTask.addParam(args[j+1]);
                    j+=3;
                }
                else if (arg.equals("-border")) {
                    tmpTask = new Task(Task.TASK_BORDER);
                    tmpTask.addParam(args[j]);
                    tmpTask.addParam(args[j+1]);
                    tmpTask.addParam(args[j+1]);
                    j+=4;
                }
                else if (arg.equals("-blur")) {
                    tmpTask = new Task(Task.TASK_BLUR);
                    tmpTask.addParam(args[j]);
                    tmpTask.addParam(args[j+1]);
                    j+=3;
                }
                else if (arg.equals("-encipher")) {
                    tmpTask = new Task(Task.TASK_ENCIPHER);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.equals("-decipher")) {
                    tmpTask = new Task(Task.TASK_DECIPHER);
                    tmpTask.addParam(args[j]);
                    j+=2;
                }
                else if (arg.substring(0, 1).equals("-"))
                    System.out.println("Unsupported funciton");

                if (tmpTask != null) {
                    jj.addTask(tmpTask);
                    tmpTask = null;
                }
            } // end loop args
            System.out.print("..created. ");
            System.out.print("Job number: "+jj.getCommands());
            System.out.println();

            cdisrv.addJob(md5user,jj);
        }
    }
}