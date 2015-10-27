package com.howtodoinjava.dom;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.Collections;
import java.util.Timer;

public class ParseKnownXMLStructure {
	//create vm array to store VMs informations
	static ArrayList<vm> vmArr;
    static ArrayList<Double> memArr = new ArrayList<Double>();
    public int minIndex;
	//public static double[] memArr;
    
    public ParseKnownXMLStructure() throws ParserConfigurationException, SAXException, IOException {
    	//Get Docuemnt Builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			 
			//Build Document
			Document document = builder.parse(new File("vm.xml"));
			 
			//Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();
			 
			//Here comes the root node
			Element root = document.getDocumentElement();
			 
			//Get all employees
			NodeList nList = document.getElementsByTagName("VM");
			
			//storing VM objects to array (hedi)
			vmArr = new ArrayList<vm>();
					
			//iterating through the XML file
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
			 Node node = nList.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE)
			 {
			    Element eElement = (Element) node;
			    //insert vm info from xml file to vmArr (hedi)
			    vmArr.add(new vm(eElement.getElementsByTagName("ID").item(0).getTextContent(), eElement.getElementsByTagName("IP").item(0).getTextContent(),0,0));
			 }
			}

			System.out.println("VMs count: " + vmArr.size());
			
			//long start_time = System.nanoTime();
			
	        for(int i=0; i<vmArr.size(); i++){
	        	double usedMem=vmArr.get(i).checkmem();
	        	System.out.println("Used Memory VM" + i+ ": "+usedMem+" %");
	        	vmArr.get(i).mem=usedMem;
	        	//vmArr.get(i).cpu=usedCpu;
	        	//System.out.println("Used CPU VM" + i+ ": "+usedCpu+" %\n");    		
	    		//vmArr.get(0).checkproc();
	            //System.out.println("Count is: " + i;
	        }
	      
			//long end_time = System.nanoTime();	double difference = (end_time - start_time)/1e6;
			//System.out.println("Execution time: "+difference+" ms");
					
			//====================================================
			//accepting new job
	        //iterating from lowest VM index (VM0) to see if it's below 70%
	        //if yes submit. if there is no VM below 70, create new VM then submit
			//====================================================
			/*
	        for(int i=0; i<vmArr.size()-1; i++){
	        	if (vmArr.get(i).mem<70) {
	        		System.out.println("less than 70%, submit job");
	                break;
	        	}
	        	System.out.println("no VM less than 70%, create new VM");
	        }
	        */
    }
	
	public static void main(String[] args) throws Exception {
        ParseKnownXMLStructure ps = new ParseKnownXMLStructure();
        
        ps.VMMonitor();
        System.out.println("asdsdasob");
        ps.checkVMfornewjob();
        int lastvm=vmArr.size()-1;
        System.out.println("Lastvm ip"+vmArr.get(lastvm).ip);

        InetAddress address = InetAddress.getByName(vmArr.get(lastvm).ip);
        if (address.isReachable(1000)) //1000s timeout
        System.out.printf("%s is reachable%n", address);
        else
        System.out.printf("%s could not be contacted%n", address);
        
		//Process p = Runtime.getRuntime().exec("ssh -t cld1593@"+address+" sh /home/cld1593/log/util.sh");
        
        //SSHCommandExecutor.class.;
	}
	

	public void VMMonitor() {
		//periodically check VM utilization, pout the result in memArr
		 Thread thread = new Thread() {
	        	public void run() {
			        while (true){
				        for(int i=0; i<vmArr.size(); i++){
				        	memArr.add(i,vmArr.get(i).mem);
				        }
				    	minIndex = memArr.indexOf(Collections.min(memArr));
				    	//if least utilized VM is less than 70%, create new VM
				    	if (memArr.get(minIndex) > 70) {
				    		System.out.println("create new VM,submit job");
				    	}
				    	else if (memArr.get(minIndex)<20) {
				    		System.out.println("VM" +minIndex +" utlization is less than 20%, remove it from VM pool");
				    	} 
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
	
	public void checkVMfornewjob() {
		System.out.println("VM with most free resource: VM" +minIndex +", submit job to this VM ");
		//creating VM code goes here
		//next is try to put cron job to this VM
		
	}
	

}
