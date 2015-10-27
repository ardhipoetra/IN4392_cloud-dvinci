package com.howtodoinjava.dom;
import java.io.*;

public class vm {
	public String id;
	public String hostname;
	public String ip;
	public double mem;
	public double cpu;
	
	public vm(String vmid, String vmhost, String vmip, double vmmem, double vmcpu) {
        id = vmid;
        hostname = vmhost;
        ip = vmip;
        mem = 0;
        cpu = 0;
    }

	public double checkmem(){
	    String s = null;
	    double usedMem=0;
		try {
			//Process p = Runtime.getRuntime().exec("ssh cld1594@fs3.das4.tudelft.nl");
			Process p = Runtime.getRuntime().exec("sh /home/hedi/workspace/JavaDOMCoba/lib/mem.sh "+ip);
	        BufferedReader stdInput = new BufferedReader(new
	             InputStreamReader(p.getInputStream()));
	
	        BufferedReader stdError = new BufferedReader(new
	             InputStreamReader(p.getErrorStream()));
	        //System.out.println("Memory Usage for "+ip+": ");
	    
	    while ((s = stdInput.readLine()) != null) {
			 //System.out.println(s+ " %");
			 usedMem = Double.parseDouble(s);
	        }	        
	    }
	    
	    catch (IOException e) {
	        System.out.println("exception happened - here's what I know: ");
	        e.printStackTrace();
	        System.exit(-1);
	    }
        return usedMem;
	}


	public double checkcpu(){
	    String s = null;
	    double usedCpu=0;
		try {
			//Process p = Runtime.getRuntime().exec("ssh cld1594@fs3.das4.tudelft.nl");
			Process p = Runtime.getRuntime().exec("sh /home/hedi/workspace/JavaDOMCoba/lib/proc.sh "+ip);
	        BufferedReader stdInput = new BufferedReader(new
	             InputStreamReader(p.getInputStream()));
	
	        BufferedReader stdError = new BufferedReader(new
	             InputStreamReader(p.getErrorStream()));
	        //System.out.println("CPU Usage for "+ip+": ");
	
	
	    while ((s = stdInput.readLine()) != null) {
	    	usedCpu = Double.parseDouble(s);
	        }              
	    }
	    catch (IOException e) {
	        System.out.println("exception happened - here's what I know: ");
	        e.printStackTrace();
	        System.exit(-1);
	    }
        return usedCpu;
	}
}

