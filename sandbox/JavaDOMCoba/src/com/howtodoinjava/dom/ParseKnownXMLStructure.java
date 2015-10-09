package com.howtodoinjava.dom;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseKnownXMLStructure {
	ArrayList<vm> vmArr;
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
		//System.out.println("root atas : "+root.getNodeName());
		 
		//Get all employees
		NodeList nList = document.getElementsByTagName("VM");
		//System.out.println("============================");
		
		//storing VM objects to array
		ArrayList<vm> vmArr = new ArrayList<vm>();
				
		//iterating through the XML file
		for (int temp = 0; temp < nList.getLength(); temp++)
		{
		 Node node = nList.item(temp);
		 //System.out.println("");    //Just a separator
		 if (node.getNodeType() == Node.ELEMENT_NODE)
		 {
		    Element eElement = (Element) node;
		    //vms.newInstance(eElement.getElementsByTagName("ID"),eElement.getElementsByTagName("IP"));
		    //Integer.parseInt(s)eElement.getElementsByTagName("IP").item(0).getTextContent();
		    vm vm1 = new vm(eElement.getElementsByTagName("ID").item(0).getTextContent(), eElement.getElementsByTagName("IP").item(0).getTextContent());
			//vm1.checkmem();
		    vmArr.add(new vm(eElement.getElementsByTagName("ID").item(0).getTextContent(), eElement.getElementsByTagName("IP").item(0).getTextContent()));
		 }
		}

		System.out.println("VMs count: " + vmArr.size());
		vmArr.get(1).checkmem();
	}
}
