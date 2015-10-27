package nl.tudelft.in4392.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

public class Job implements Serializable{

    private static final long serialVersionUID = -5186370518480604238L;

    public static final int JOB_CREATED = 0;
    public static final int JOB_WAITING = 1;
    public static final int JOB_RUNNING = 2;
    public static final int JOB_FINISH = 9;
    public static final int JOB_ERROR = 88;


    public int id;
    public String name;
    public String URI;
    public String uid;

    private String filename;
    private String extension;
    private String exURI;
    private String destfile;

    public int status = -1;

    public ArrayList<Task> tasks;

	public Job(String name, String URI, String uid) {
		this.name = name;
        this.URI = URI;
        tasks = new ArrayList<Task>();
        this.uid = uid;
        this.status = JOB_CREATED;
	}

    public void setDestPath(String dpath) {
        this.destfile = dpath;
    }

    public boolean getInfo(String path, String expath) {
        File f = new File(path);
        if(f.exists() && !f.isDirectory())
            URI = path;
        else
            return false;

        filename = FilenameUtils.getBaseName(path);
        extension = FilenameUtils.getExtension(path);

        this.destfile = path;

        if (".".equals(expath))
            exURI = FilenameUtils.getFullPath(URI);
        else
            exURI = expath;

        return true;
    }

    public boolean modifyName(String fn) {
        if(!"".equals(fn)) {
            filename = fn;
            refreshDestURI();
            return true;
        }
        else
            return false;
    }

    public boolean modifyExtension(String ext) {
        if(!"".equals(ext)) {
            extension = ext;
            refreshDestURI();
            return true;
        }
        else
            return false;
    }

    private void refreshDestURI() {
        destfile = exURI + filename + "." + extension;
    }

    @Override
    public java.lang.String toString() {
        String st = "";
        for(Task t : tasks) {
             st += "(T : "+t.action+")";
        }

        return String.format("The job(%s) [%d] by %s: %s %s", name, status, uid, URI, st);
    }

    public void addTask(Task t) {this.tasks.add(t);}

    public String getCommands() {
        String cmd = "java -cp .:../lib/* nl.tudelft.vmlocal.LocalMain";

        cmd += " "+this.URI+" "+this.destfile;

        for(Task t : tasks) {
            cmd += " "+t.generateParam();
        }

        return cmd;
    }
}
