package com.howtodoinjava.dom;

import java.io.BufferedReader;
import java.lang.String;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Text;
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
    static ArrayList<Double> procArr = new ArrayList<Double>();
    static ArrayList<String> idArr = new ArrayList<String>();
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

			Element firstNode = (Element) nList.item( 0 );
			//storing VM objects to array (hedi)
			vmArr = new ArrayList<vm>();

			//iterating through the XML file
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
			 Node node = nList.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE)
			 {
			    Element eElement = (Element) node;
			    vmArr.add(new vm(eElement.getElementsByTagName("ID").item(0).getTextContent(),
			    		eElement.getElementsByTagName("HOSTNAME").item(1).getTextContent(),
			    		eElement.getElementsByTagName("IP").item(0).getTextContent(),0,0));
			 }
			}

			//System.out.println("VMs count: " + vmArr.size());

			//long start_time = System.nanoTime();
			/*
	        for(int i=0; i<vmArr.size(); i++){
	        	double usedMem=vmArr.get(i).checkmem();
	        	System.out.println("Used Memory VM" + i+ ": "+usedMem+" %");
	        	vmArr.get(i).mem=usedMem;
	        	//vmArr.get(i).cpu=usedCpu;
	        	//System.out.println("Used CPU VM" + i+ ": "+usedCpu+" %\n");
	    		//vmArr.get(0).checkproc();
	            //System.out.println("Count is: " + i;
	        }
	        */

    }

	public static void main(String[] args) throws Exception {
        ParseKnownXMLStructure ps = new ParseKnownXMLStructure();


        ps.checkVMfornewjob();
        ps.parsinglogfile();
        ps.VMMonitor();

        //move this block to checkVMfornewjob below
        //============
        int lastvm=vmArr.size()-1;
        //System.out.println("Lastvm ip"+vmArr.get(lastvm).ip);

        InetAddress address = InetAddress.getByName(vmArr.get(lastvm).ip);
        if (address.isReachable(1000)) {//1000s timeout
	        //System.out.printf("%s is reachable%n", address);
			//SSHCommandExecutor.sesuatu();
	        //get latest created VM. Ask it to run log file script
	        Runtime.getRuntime().exec("ssh cld1593@"+vmArr.get(lastvm).ip+" sh /home/cld1593/log/util.sh "+vmArr.get(lastvm).id);
	        //todo: log file format
        }
        else
        System.out.printf("%s could not be contacted%n", address);
        //=============
	}


	public void VMMonitor() {
		 Thread thread = new Thread() {
	        	public void run() {
			        while (true){
			        	//System.out.println(procArr.size());
			      		//System.out.println(procArr.get(1));
			      		/*
				        for(int i=0; i<vmArr.size(); i++){
				        	memArr.add(i,vmArr.get(i).mem);
				        } */
				    	minIndex = procArr.indexOf(Collections.min(procArr));

				    	//create and delete VM rules goes here. based on proArr
				    	System.out.println("VM with least util: "+idArr.get(minIndex));

				    	try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			        }
	        	}

	        };
	        thread.start();
	}

	public void checkVMfornewjob() {
		//System.out.println("VM with most free resource: VM" +minIndex +", submit job to this VM ");
		//creating VM code goes here
		//next is try to put cron job to this VM
	}

	public void parsinglogfile() throws Exception {
		//this function reads log file. put the result in memArray, procArray and id Array
		//these three arrays is like relational arrays, related with the indexes

        for(int i=0; i<vmArr.size(); i++){
    		//get log file. format: vm_id.log
    		FileInputStream in = new FileInputStream("/home/cld1593/log/"+vmArr.get(i).id+".log");
    		//System.out.println(vmArr.get(i).id);
  		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
  		  String strLine = null, tmp;
  		  while ((tmp = br.readLine()) != null)
  		  {
  		     strLine = tmp;
  		  }
  		  //read last line, split the mem and proc util, put into array
  		  String lastLine = strLine;
  		  memArr.add(i,vmArr.get(i).mem);
  		  String[] array = strLine.split(" ", -1);
  		  //System.out.println(array[1]);
  		  in.close();
  		  //System.out.println(search(vmArr.get(i).id,idArr));
  		  if (search(vmArr.get(i).id,idArr)==0)
  		  {
  			//if the id is not exist in idArr is not exist, add new element
  			idArr.add(vmArr.get(i).id);
  			memArr.add(Double.parseDouble(array[0]));
  			procArr.add(Double.parseDouble(array[1]));
  		  }
  		  else
  		  {
  			//if idArr exists, replace the value
  			idArr.add(search(vmArr.get(i).id,idArr), vmArr.get(i).id);
  			memArr.add(search(vmArr.get(i).id,idArr), Double.parseDouble(array[0]));
  			procArr.add(search(vmArr.get(i).id,idArr), Double.parseDouble(array[0]));
  		  }
        }
	}


	public static int search(String searchStr, ArrayList<String> aList)
		{
		boolean found = false;
		Iterator<String>  iter = aList.iterator();
		String curItem="";
		int pos=0;

		while ( iter .hasNext() == true )
		{
		    pos=pos+1;
		    curItem =(String) iter .next();
		    if (curItem.equals(searchStr)  ) {
		    found = true;
		    break;
		        }
		}
		if ( found == false ) {
		pos=0;
		}

		if (pos!=0)
		 {
			System.out.println(searchStr + " Found in position : " + pos);
		 }
		else
		 {
		    //System.out.println(searchStr + " Not found");
		 }
		return pos;
	}
}
