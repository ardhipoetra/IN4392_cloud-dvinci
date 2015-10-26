package nl.tudelft.in4392.manager;

import nl.tudelft.in4392.Constants;
import nl.tudelft.in4392.Utility;
import nl.tudelft.in4392.model.VinciVM;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class VMmanager {

    public static Client c;

    public static HashMap<Integer, VinciVM> vmList = new HashMap<Integer, VinciVM>();

	public static VinciVM createVM() throws Exception{

        FileInputStream fileInputStream = new FileInputStream(Constants.VM_TEMPLATE_DEFAULT);
        byte[] buffer = new byte[fileInputStream.available()];
        int length = fileInputStream.read(buffer);
        fileInputStream.close();

        String vmtemplate = new String(buffer, 0, length, Charset.forName("UTF-8"));

        try {
            if (c == null)
                c = new Client(Constants.CREDENTIALS, null);
        } catch (ClientConfigurationException e) {
            e.printStackTrace();
        }

        OneResponse rc = VirtualMachine.allocate(c, vmtemplate);

        if(rc.isError()) {
            System.out.println( "failed!");
            throw new Exception( rc.getErrorMessage() );
        }

        int newVMID = Integer.parseInt(rc.getMessage());
        System.out.println("ok, ID " + newVMID + ".");

        VinciVM vm = new VinciVM(newVMID, c);

        rc = vm.info();

        if(rc.isError())
            throw new Exception( rc.getErrorMessage() );

        System.out.println("The new VM " + vm.getName() + " has status: " + vm.status());

        return vm;
    }

    public static VinciVM getVM(int id) {
        return vmList.get(id);
    }

    public static OneResponse deleteVM(VirtualMachine vm) {
        return null;
    }

    public static String showVMInfo(VirtualMachine vm) {
        return "";
    }

    public static String showAllVms() throws Exception {
        try {
            if (c == null)
                c = new Client(Constants.CREDENTIALS, null);
        } catch (ClientConfigurationException e) {
            e.printStackTrace();
        }

        VirtualMachinePool vpool = new VirtualMachinePool(c);

        String allXmlVm = vpool.infoAll().getMessage();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new InputSource(new StringReader(allXmlVm)));
        document.getDocumentElement().normalize();

        NodeList nList = document.getElementsByTagName("VM");

        String ret = "";

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;

                VinciVM vmTmp = parseXML(Utility.nodeToString(eElement));

                ret += "VM ("+vmTmp.id+") IP : "+vmTmp.ip;
                ret += "\n";
            }
        }

        return ret;
    }

    public static VirtualMachine getAvailableVM() {
        return new VirtualMachine(40885, c);
    }


    public static VinciVM parseXML(String s) throws  Exception{

        try {
            if (c == null)
                c = new Client(Constants.CREDENTIALS, null);
        } catch (ClientConfigurationException e) {
            e.printStackTrace();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Build Document
        Document document = builder.parse(new InputSource(new StringReader(s)));

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element eElement = document.getDocumentElement();

        VinciVM ret = new VinciVM(Integer.parseInt(eElement.getElementsByTagName("ID").item(0).getTextContent()),
                c,eElement.getElementsByTagName("IP").item(0).getTextContent(),
                0,
                0);

        ret.setHostname(eElement.getElementsByTagName("HOSTNAME").item(0).getTextContent());
        ret.setXmlData(s);

        vmList.put(ret.id, ret);
        return ret;
    }
}
