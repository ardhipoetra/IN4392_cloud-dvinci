package nl.tudelft.in4392.server;

import nl.tudelft.in4392.model.Job;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by ardhipoetra on 10/6/15.
 */
public interface _CDvinci extends Remote {

    public Job addJob(String userID, Job j) throws  RemoteException;



    public int getTotalJobs(String userID) throws RemoteException;
    public void printJobs(String userID) throws RemoteException;
    public int call(String userID, String s) throws RemoteException;
//
//    public Job setImgPath(Job name) throws RemoteException;
//    public void applyTask(Task t) throws RemoteException;
//    public void execute() throws RemoteException;
}
