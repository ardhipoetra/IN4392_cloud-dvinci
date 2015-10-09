package nl.tudelft.in4392.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Job implements Serializable{

    private static final long serialVersionUID = -5186370518480604238L;

    public static final int JOB_CREATED = 0;
    public static final int JOB_SUBMITTED = 1;
    public static final int JOB_RUNNING = 2;
    public static final int JOB_FINISH = 9;


    public int id;
    public String name;
    public String imgURI;

    public int status = -1;

    public ArrayList<Task> tasks;

	public Job(String name, String imgURI) {
		this.name = name;
        this.imgURI = imgURI;
        tasks = new ArrayList<Task>();
	}

    public Job() {this.name = "temp";}

    @Override
    public java.lang.String toString() {
        String st = "";
        for(Task t : tasks) {
             st += "(T : "+t.action+")";
        }

        return String.format("The job(%s) [%d] : %s %s", name, status, imgURI, st);
    }

    public void addTask(Task t) {this.tasks.add(t);}


}
