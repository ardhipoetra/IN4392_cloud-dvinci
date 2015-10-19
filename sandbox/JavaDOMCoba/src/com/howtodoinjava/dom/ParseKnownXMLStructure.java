package com.howtodoinjava.dom;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collections;

public class ParseKnownXMLStructure {
	//create vm array to store VMs informations
	ArrayList<vm> vmArr;
    static ArrayList memArr = new ArrayList();
	//public static double[] memArr;
	
	public static void main(String[] args) throws Exception {
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
		ArrayList<vm> vmArr = new ArrayList<vm>();
				
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
		
		long start_time = System.nanoTime();

        for(int i=0; i<vmArr.size(); i++){
        	double usedMem=vmArr.get(i).checkmem();
        	double usedCpu=vmArr.get(i).checkcpu();
        	System.out.println("Used Memory VM" + i+ ": "+usedMem+" %");
        	vmArr.get(i).mem=usedMem;
        	vmArr.get(i).cpu=usedCpu;
        	System.out.println("Used CPU VM" + i+ ": "+usedCpu+" %\n");    		
    		//vmArr.get(0).checkproc();
            //System.out.println("Count is: " + i;
        }
      
		long end_time = System.nanoTime();	double difference = (end_time - start_time)/1e6;
		System.out.println("Execution time: "+difference+" ms");
		
		
		//====================================================
		//checking VM pool resource regularly
		//if used resource is more than 70%, create new VM
		//iterating from highest index VM
		//====================================================
        for(int i=vmArr.size()-1; i>0; i--){
        	if (vmArr.get(i).mem>70) {
        		System.out.println("create new VM");
        	}
    		if (vmArr.get(i).mem<20) {
    			System.out.println("remove this VM");
    		}
        }
		
		//====================================================
		//accepting new job
        //iterating from lowest VM index (VM0) to see if it's below 70%
        //if yes submit. if there is no VM below 70, create new VM then submit
		//====================================================
        for(int i=0; i<vmArr.size()-1; i++){
        	if (vmArr.get(i).mem<70) {
        		System.out.println("less than 70%, submit job");
                break;
        	}
        	System.out.println("no VM less than 70%, create new VM");
        }

		//====================================================
		//checking VM pool resource regularly (different method from above)
		//minIndex=least used VM
		//====================================================
        for(int i=0; i<vmArr.size(); i++){
        	memArr.add(i,vmArr.get(i).mem);
        }
    	int minIndex = memArr.indexOf(Collections.min(memArr));
    	System.out.println("VM with most free resource:" +minIndex);
		
	}
}
