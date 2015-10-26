package nl.tudelft.in4392;

import cabbott.net.SSHManager;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by ardhipoetra on 10/9/15.
 */
public class Utility {
    public static int callSSH(String target, String command) {
        SSHManager instance = new SSHManager(target, Constants.SSH_KNOWN_HOSTS);
        String errorMessage = instance.connect();

        if(errorMessage != null)
        {
            System.out.println(errorMessage);
            return -1;
        }

        String result = instance.sendCommand(command);
        instance.close();

        System.out.println("res : "+result);
        return 0;
    }

    public static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

}
