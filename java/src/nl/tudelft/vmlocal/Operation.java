package nl.tudelft.vmlocal;

import org.apache.commons.io.FilenameUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ardhipoetra on 10/26/15.
 */
public class Operation {
    ConvertCmd cc = new ConvertCmd();
    IMOperation im = new IMOperation();

    private String filename;
    private String extension;
    private String URI;
    private String exURI;
    private String destfile;

    public boolean GetInfo(String path, String expath) {
        File f = new File(path);
        if(f.exists() && !f.isDirectory())
            URI = path;
        else
            return false;

        filename = FilenameUtils.getBaseName(path);
        extension = FilenameUtils.getExtension(path);

        destfile = path;

        if (".".equals(expath))
            exURI = FilenameUtils.getFullPath(URI);
        else
            exURI = expath;

        SetPrimaryImage();
        return true;
    }

    public boolean ModifyName(String fn) {
        if(!"".equals(fn)) {
            filename = fn;
            RefreshDestURI();
            return true;
        }
        else
            return false;
    }

    public boolean ModifyExtension(String ext) {
        if(!"".equals(ext)) {
            extension = ext;
            RefreshDestURI();
            return true;
        }
        else
            return false;
    }

    private void RefreshDestURI() {
        destfile = exURI + filename + "." + extension;
    }

    public void GetItDone() {
        SetSlaveImage();
        try {
            cc.run(im);
        }
        // can't do this in JDK6
//        catch (IOException | InterruptedException | IM4JavaException ex) {
//            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
//        }
        catch (Exception e) {

        }
    }

    public String ReturnPath() {
        return destfile;
    }

    private void SetPrimaryImage() {
        im.addImage(URI);
    }

    private void SetSlaveImage() {
        im.addImage(destfile);
    }

    public void scaletox(String sizex) {
        im.scale(Integer.parseInt(sizex));
    }

    public void scaletoy(String sizey) {
        im.scale(null, Integer.parseInt(sizey));
    }

    public void resize(String sizex, String sizey) {
        im.resize(Integer.parseInt(sizex), Integer.parseInt(sizey), "!");
    }

    public void rotate(String param) {
        im.rotate(Double.parseDouble(param));
    }

    public void colorspace(String cspace) {
        im.colorspace(cspace);
    }

}
