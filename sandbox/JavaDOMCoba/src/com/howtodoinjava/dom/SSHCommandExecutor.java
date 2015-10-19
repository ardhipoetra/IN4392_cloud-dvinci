package com.howtodoinjava.dom;
import java.io.InputStream;
 
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
 
 
public class SSHCommandExecutor {
 
    /** 
     * @param args
     */
    public static void main(String[] args) {
        String host="fs3.das4.tudelft.nl";
        String user="cld1594";
        String password="Yp08fdaR";
        String command1="free|grep Mem|awk '{print $4/$2*100.0}'";
        
		long start_time = System.nanoTime();			
		
        try{
             
            java.util.Properties config = new java.util.Properties(); 
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session=jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            //System.out.println("Connected");
             
            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command1);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
             
            InputStream in=channel.getInputStream();
            channel.connect();
            byte[] tmp=new byte[1024];
            while(true){
              while(in.available()>0){
                int i=in.read(tmp, 0, 1024);
                if(i<0)break;
                System.out.print(new String(tmp, 0, i));
              }
              if(channel.isClosed()){
                //System.out.println("exit-status: "+channel.getExitStatus());
                break;
              }
              try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            session.disconnect();
            //System.out.println("DONE");
        }catch(Exception e){
            e.printStackTrace();
        }

		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;
		System.out.println("Execution time: "+difference+" ms");
 
    }
 
}