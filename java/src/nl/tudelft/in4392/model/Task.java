package nl.tudelft.in4392.model;

import java.io.Serializable;

public class Task implements Serializable{

    private static final long serialVersionUID = -6560961321313608350L;

    public static final String TASK_RESIZE = "resize";
    public static final String TASK_COMPOSITE = "composite";
    public static final String TASK_FILTER = "filter";

    public String action;

    public Task(String act) {
        this.action = act;
    }
}
