
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] array;
		try {
			// 상대방이 연결할수 있도록 UDP 소켓 생성
			DatagramSocket dsoc = new DatagramSocket(9999);
			// 전송받은 데이터를 지정할 바이트 배열선언
			byte[] date = new byte[66536];

			// UDP 통신으로 전송을 받을 packet 객체생성
			DatagramPacket dp = new DatagramPacket(date, date.length);

			System.out.println("데이터 수신 준비 완료....");
			while (true) {
				// 데이터 전송 받기
				dsoc.receive(dp);
				// 데이터 보낸곳 확인
				System.out.println(" 송신 IP : " + dp.getAddress());
				// 보낸 데이터를 Utf-8에 문자열로 벼환
				String msg = new String(dp.getData(), "UTF-8");
				System.out.println("보내 온 내용  : " + msg);

				array = msg.split("::");
				String sender_id = array[0];

				/**********************************
				 * ///////////////전송/////////////////
				 **********************************/
				// 전송받은 데이터를 그대로 반송
				DatagramPacket sendPacket = new DatagramPacket(
						sender_id.getBytes(),
						sender_id.getBytes().length,
						dp.getAddress(), dp.getPort());
				dsoc.send(sendPacket);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
