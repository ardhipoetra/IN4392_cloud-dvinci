package com.howtodoinjava.dom;
import java.io.*;

public class vm {
	public String id;
	public String ip;
	
	public vm(String vmid, String vmip) {
        id = vmid;
        ip = vmip;
    }

	public void checkmem(){
		//System.out.println(""+id);
	
		try {
	        Process p = Runtime.getRuntime().exec("sh mem.sh "+ip);
	        BufferedReader stdInput = new BufferedReader(new
	             InputStreamReader(p.getInputStream()));
	
	        BufferedReader stdError = new BufferedReader(new
	             InputStreamReader(p.getErrorStream()));
	        System.out.println("Memory Usage: ");

	    String s = null;
	    Double usedMem;
	    while ((s = stdInput.readLine()) != null) {
	            usedMem = Double.parseDouble(s);
		System.out.println(usedMem+ " %");
	        }              
	    }
	    catch (IOException e) {
	        System.out.println("exception happened - here's what I know: ");
	        e.printStackTrace();
	        System.exit(-1);
	    }
	}
}
