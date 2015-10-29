package nl.tudelft.in4392.model;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.core.Operation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class Task implements Serializable{

    private static final long serialVersionUID = -6560961321313608350L;

    public static final String TASK_RESIZE = "resize";
    public static final String TASK_ROTATE = "rotate";
    public static final String TASK_SCALEX = "scaletox";
    public static final String TASK_SCALEY = "scaletoy";
    public static final String TASK_COLORSPACE = "colorspace";

    public static final String TASK_BLUR = "blur";
    public static final String TASK_ENCIPHER = "encipher";
    public static final String TASK_SHARPEN = "sharpen";

    private ArrayList<String> params;
    public String action;

    public Task(String act) {
        this.action = act;
        this.params = new ArrayList<String>();
    }

    public int addParam(String param) {
        this.params.add(param);
        return params.size();
    }

    public String generateParam() {
        String sPar = " ";
        Iterator<String> is = params.iterator();

        while(is.hasNext()) {
            sPar += is.next();
            if (is.hasNext()) sPar += " ";
        }

        return "-"+action+sPar;
    }
}
