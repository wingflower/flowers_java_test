package sender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws Exception {
		
		
//			System.out.println("ip : " + Inet4Address.getLocalHost().getHostAddress());
//			System.out.println("ip : " + Inet4Address.getLocalHost().getHostAddress());
//			while(NetworkInterface.getNetworkInterfaces().hasMoreElements()) {
//				System.out.println(NetworkInterface.getNetworkInterfaces().nextElement());
//			}
		
		Runtime runtime = Runtime.getRuntime();
		Process process; 
        String res = "-0-"; 
        try { 
                String cmd = "cat /proc/meminfo"; 
                process = runtime.exec(cmd); 
                
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream())); 
                String line ; 
                System.out.println("dasd");
            
                while ((line = br.readLine()) != null) {
                	System.out.println("test :: " + line);
                } 
        } catch (Exception e) { 
                e.fillInStackTrace(); 
                System.out.println("Process Manager Unable to execute top command"); 
        }
	}
}