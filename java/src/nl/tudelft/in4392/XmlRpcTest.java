package nl.tudelft.in4392;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class XmlRpcTest {
    public static void main( String args[] ) throws XmlRpcException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();


        XmlRpcClient client;
        try {
            config.setServerURL(new URL("http://localhost:2633/RPC2"));
        } catch (MalformedURLException e) {
            String msg = String.format(
                    "Invalid URL for XML-RPC server. Message=%s.",
                    e.getMessage()
            );
        }
        config.setBasicUserName("cld1593");
        config.setBasicPassword("w96dxdOJ");
        /* initialize client */
        client = new XmlRpcClient();
        client.setConfig(config);

        String command = "one.vm.info";
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(40884);
        parameters.add(0,"cld1593:w96dxdOJ");

        Object[] result = (Object[]) client.execute(command, parameters);

        if (result != null) {
            Boolean successful = (Boolean) result[0];
            if (successful) {
                System.out.println(result[1]);
            } else {
                System.out.printf("Execution failed. ErrorCode=%d. Message=%s.\n", (Integer) result[2], (String) result[1]);
            }
        }
    }
}
