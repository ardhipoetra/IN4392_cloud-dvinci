import java.io.*;
 
public class JavaRunCommand {
 
    public static void main(String args[]) {
	if (args.length == 0) {
		System.out.println("VM ip required");
		System.exit(-1);
	}	
	String ip = args[0]; 
        String s = null;
	Double number;
	//String ip = "10.141.3.153"; 
        try {
            Process p = Runtime.getRuntime().exec("sh mem.sh "+ip);
            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
 
            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));
            System.out.println("Memory Usage: ");
            
	    while ((s = stdInput.readLine()) != null) {
                number = Double.parseDouble(s);
		System.out.println(number+ " %");
            }              
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }

//--------------------------------------------------------------------
        try {
            Process p = Runtime.getRuntime().exec("sh proc.sh "+ip);
             
            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
 
            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));
            System.out.println("Processor Usage: ");
            
	    while ((s = stdInput.readLine()) != null) {
                 number = Double.parseDouble(s);
	    	 System.out.println(number+ " %");
            }
             
            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
