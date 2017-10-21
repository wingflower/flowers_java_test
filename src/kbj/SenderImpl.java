package kbj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SenderImpl {

	public static void main(String[] args) {
		// 키보드 입력 받기 위한 변수
		// Scanner scanner = new Scanner(System.in);
		// System.out.println("보낼 메세지 입력 :");
		// String msg = scanner.next();

		String msg = "SenderID::Contents";
		

		try {
			// 전송할 수 있는 UDP 소켓 생성
			DatagramSocket dsoc = new DatagramSocket();
			System.out.println("UDP 소켓 생성");

			// 받을 곳의 주소 생성
			InetAddress ia = InetAddress.getByName("192.168.1.8");
			System.out.println(ia.getHostAddress() + " 주소 생성 ");

			// 전소할 데이터 생성
			DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ia, 9999);
			byte[] data = dp.getData();

			// epdlxj wjsthd
			dsoc.send(dp);
			System.out.println("데이터 보냄 ");

			/**********************************
			 * ///////////////수신/////////////////
			 **********************************/
			while (true) {
				byte[] buffer = new byte[msg.getBytes().length];
				// 반송되는 DatagramPacket을 받기 위해 receivePacket 생성한 후 대기
				DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
				dsoc.receive(receivePacket);

				// 받은 결과 출력
				String receviedMsg = new String(receivePacket.getData(), 0, receivePacket.getData().length);
				System.out.println("전송받은 문자열 : " + receviedMsg);
				
				if(receviedMsg.length() != 0) {
					break;
				}
			}

			dsoc.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// public static void main(String[] args) {
	//
	// String ip = "192.168.1.8";
	// int port = 9999;
	//
	// InetAddress inetaddr = null;
	//
	// try {
	// // DatagramPacket에 들어갈 ip 주소가 InetAddress 형태여야 함
	// inetaddr = InetAddress.getByName(ip);
	// } catch (UnknownHostException e) {
	// System.out.println("잘못된 도메인이나 ip입니다.");
	// System.exit(1);
	// }
	//
	// /**********************************
	// * ///////////////전송/////////////////
	// **********************************/
	// DatagramSocket dsock = null;
	//
	// try {
	// // 키보드로부터 서버에게 전송할 문자열을 입력받기 위해
	// // System.in을 BufferedReader 형태로 변환
	// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	// dsock = new DatagramSocket();
	//
	// String line = "SenderId::Contents";
	// while ((line = br.readLine()) != null) {
	//
	// // DatagramPacket에 각 정보를 담고 전송
	//
	// DatagramPacket sendPacket = new DatagramPacket(line.getBytes(),
	// line.getBytes().length, inetaddr, port);
	// dsock.send(sendPacket);
	//
	//// if (line.equals("quit"))
	//// break;
	//
	// /**********************************
	// * ///////////////수신/////////////////
	// **********************************/
	// byte[] buffer = new byte[line.getBytes().length];
	// // 반송되는 DatagramPacket을 받기 위해
	// // receivePacket 생성한 후 대기
	// DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
	// dsock.receive(receivePacket);
	//
	// // 받은 결과 출력
	// String msg = new String(receivePacket.getData(), 0,
	// receivePacket.getData().length);
	// System.out.println("전송받은 문자열 : " + msg);
	//
	// }
	// System.out.println("UDPEchoClient를 종료합니다.");
	// } catch (Exception ex) {
	// System.out.println(ex);
	// } finally {
	// if (dsock != null)
	// dsock.close();
	// }
	// }
}
