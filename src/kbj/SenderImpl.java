package kbj;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SenderImpl {
	
	public static void main(String[] args) throws Exception {
		
		String clientIP = Inet4Address.getLocalHost().getHostAddress();
		String mgmtIp = "10.101.30.176";
		int mgmtPort = 30000;
		String msg = "[meta]cpu,mem," + clientIP + ":" + mgmtPort;

		// it gets a collector ID and sender ID from the mgmt		
		Map<String, String> ids = getIds(msg, mgmtIp, mgmtPort);
		
//		String collectorId = ids.get("collectorId"); 
		String senderId = ids.get("senderId");
		
		String[] collectorAddress = ids.get("collectorIp").split(":");
		
		String collectorIp = collectorAddress[0];
		int collectorPort = Integer.parseInt(collectorAddress[1].trim());
				
		// it keep checking the connection till it connects to the collector
		checkConnection(collectorIp, collectorPort, senderId);
		
		// it starts to send logs
		sendLog(senderId, collectorIp, collectorPort);
	}

	/**
	 * It gets a collector id and a sender id from mgmt
	 * @author Bum-jin Kim
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("resource")
	public static Map<String, String> getIds(String msg, String mgmtIp, int mgmtPort) {
		try {
			// it creates a udp socket
			DatagramSocket dsoc = new DatagramSocket();
			System.out.println("UDP socket's been generated.");
			
			// it creates the mgmt ip address
			InetAddress ia = InetAddress.getByName(mgmtIp);
			System.out.println(ia.getHostAddress() + " address's been generated.");

			// it sends the msg to the mgmt
			sendPacket(ia, msg, dsoc, mgmtPort);
			
			/**********************************
			 * Wait for data
			 **********************************/
			System.out.println("waitong for data from the mgmt");
			while (true) {
//				sendPacket(ia, msg, dsoc);
				
//				byte[] buffer = new byte[msg.getBytes().length];
				byte[] buffer = new byte[1024];

				// it receives data
				DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
				dsoc.receive(receivePacket);

				// it prints the received data
				String receviedMsg = new String(receivePacket.getData(), 0, receivePacket.getData().length);
				System.out.println("The received data : " + receviedMsg);
				
				
				if(receviedMsg.isEmpty() == false) {
					System.out.println("The mgmt's been connected ");
					
					String[] splitReceivedMsg = receviedMsg.split(",");
					Map<String, String> result = new HashMap<>();
					result.put("collectorId", splitReceivedMsg[0]);
					result.put("senderId", splitReceivedMsg[1]);
					result.put("collectorIp", splitReceivedMsg[2]);
					
					dsoc.close();
					return result;
				}
				
				Thread.sleep(500);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * It keep checking the connection till it connects to the collector
	 * @author Bum-jin Kim
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public static void checkConnection(String ip, int port, String senderId) throws Exception {
		InetAddress ia = InetAddress.getByName(ip);
		DatagramSocket dsoc = new DatagramSocket();
		
		DatagramPacket dp = new DatagramPacket(senderId.getBytes(), senderId.getBytes().length, ia, port);
		
		while(true) {
			dsoc.send(dp);
			
			byte[] buffer = new byte[1024];
			// 반송되는 DatagramPacket을 받기 위해 receivePacket 생성한 후 대기
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			dsoc.receive(receivePacket);

			// 받은 결과 출력
			String receviedMsg = new String(receivePacket.getData(), 0, receivePacket.getData().length);
			System.out.println("전송받은 문자열 : " + receviedMsg);
			
			
			if(receviedMsg.isEmpty() == false) {
				System.out.println("collector 연결 확인 ");
				
				dsoc.close();
				break;
			}
			
			Thread.sleep(500);
		}
	}
	
	/**
	 * It sends logs to the ip
	 * @author 김범진A
	 * @param id
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public static void sendLog(String id, String ip, int port) throws Exception {
		
		InetAddress ia = InetAddress.getByName(ip);
		
		String clientIP = Inet4Address.getLocalHost().getHostAddress();
		DatagramSocket dsoc = new DatagramSocket();
		
		String msg = id + "," + clientIP + ":" + port + ",name|work";
		String[] name = {"KBJ", "SSY", "KDH"};
		String[] work = {"Sender", "Collector", "WatchDog"};
		
		
		DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ia, port);
		dsoc.send(dp);
		
		while(true) {
			int ran = randomRange(0, 2);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(new Date());
			
			msg = String.format("[LOG %s]%s,%s", date, name[ran], work[ran]);
			dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ia, port);
			dsoc.send(dp);
			Thread.sleep((ran+1) * 1000);
		}
		
	}
	
	/**
	 * Send the msg to the ia
	 * @author Bum-jin Kim
	 * @param ia
	 * @param msg
	 * @param dsoc
	 * @throws IOException
	 */
	public static void sendPacket(InetAddress ia, String msg, DatagramSocket dsoc, int port) throws IOException {
		DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ia, port);
		dsoc.send(dp);
		
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		sb.append(msg);
		sb.append(" ]");
		sb.append(" was sent to ");
		sb.append(ia.getHostAddress());
		
		System.out.println(sb.toString());
	}

	/**
	 * It generates a random number from n1 to n2
	 * @author Bum-jin Kim
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static int randomRange(int n1, int n2) {
		return (int) (Math.random() * (n2 - n1 + 1)) + n1;
	}

}
