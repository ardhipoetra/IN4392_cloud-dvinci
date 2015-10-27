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
import sun.management.VMManagement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.*;

public class VMmanager {

    public static Client c;

    public static HashMap<Integer, VinciVM> vmList = new HashMap<Integer, VinciVM>();
    static ArrayList<Double> memArr = new ArrayList<Double>();


    static int currentTargetVm = -1;

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

        vmList.put(newVMID, vm);
        return vm;
    }

    public static VinciVM getVM(int id) {
        return vmList.get(id);
    }

    public static OneResponse deleteVM(VinciVM vm) {
        vmList.remove(vm.id());
        OneResponse or = vm.finalizeVM();
        return or;
    }

    public static String showVMInfo(VinciVM vm) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID : ");
        sb.append(vm.id());
        sb.append('\n');
        sb.append(vm.info().getMessage());

        return sb.toString();
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

    public static VinciVM getAvailableVM() {

        while(VMmanager.currentTargetVm < 0) {

        }

        return vmList.get(VMmanager.currentTargetVm);
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

        ret.setHostname(eElement.getElementsByTagName("HOSTNAME").item(1).getTextContent());
        ret.setXmlData(s);

        vmList.put(ret.id, ret);

        System.out.println("parsed : "+ret.id);
        return ret;
    }

    public static void startVMmonitor() {
        Thread thread = new Thread() {
            public void run() {
                while (true){
                    Double minMem = Double.MAX_VALUE;
                    Iterator<Map.Entry<Integer, VinciVM>> it = vmList.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Integer,VinciVM> e = it.next();
                        if(minMem.compareTo(e.getValue().mem) > 0) {
                            VMmanager.currentTargetVm = e.getKey();
                        }
                    }
                    System.out.println("min VM target : "+VMmanager.currentTargetVm);

                    //if least utilized VM is less than 70%, create new VM
//                    if (minMem.compareTo(new Double("70")) > 0) {
//                        System.out.println("create new VM");
//
//                        try {
//                            VinciVM newvm = VMmanager.createVM();
//                            System.out.println("created VM : "+newvm.id());
//                            VMmanager.currentTargetVm = newvm.id();
//                        } catch (Exception e) {e.printStackTrace();}
//
//                    }
//                    else if (minMem.compareTo(new Double("20")) < 0 && vmList.size() > 3) {
//                        System.out.println("VM" +VMmanager.currentTargetVm +" utlization is less than 20%, remove it from VM pool");
//
//                        VMmanager.deleteVM(vmList.get(VMmanager.currentTargetVm));
//                        VMmanager.currentTargetVm = new ArrayList<Integer>(vmList.keySet())
//                                .get(new Random().nextInt(vmList.keySet().size()));
//                    }


                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        thread.start();
    }
}
