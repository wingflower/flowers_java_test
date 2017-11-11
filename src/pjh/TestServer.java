package socketTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;

public class TestServer {
	public static final int SERVER_PORT = 9999;
	private static final Logger logger = Logger.getLogger(TestServer.class.getCanonicalName());
	private static final int NUM_THREADS = 10;
	
	private Jedis jedis;

	public TestServer() {

	}

	public TestServer(int port) {
		jedis = new Jedis("localhost", 6379);
		System.out.println("Connection to REDIS Server sucessfully");
		System.out.println("REDIS Server is running: " + jedis.ping());
		
	}

	public void start() throws IOException {
		ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
		try (ServerSocket server = new ServerSocket(9999)) {
			logger.info("Accepting connections on port " + server.getLocalPort());
			while (true) {
				try {
					System.out.println("Server is waiting...");
					Socket socket = server.accept();

					Runnable r = new RequestProcessor(socket);
					pool.submit(r);

				} catch (IOException ex) {
					logger.log(Level.WARNING, "Error accepting connection", ex);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception");
		}
	}
	
	class RequestProcessor implements Runnable {
		private Socket connection;

		public RequestProcessor(Socket connection) {
			this.connection = connection;
		}

		@Override
		public void run() {
			PrintWriter pw = null;
			BufferedReader br = null;

			try {
				InetAddress addr = connection.getInetAddress();
				String ip = addr.getHostAddress();
				System.out.println(ip + "is connected");
				jedis.set("SENDER_IP", ip);

				System.out.println("jedis SENDER_IP Value : " + jedis.get("SENDER_IP"));
				System.out.println(jedis.dbSize());
				
				// JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
				// JedisPool pool = new JedisPool(jedisPoolConfig, "localhost",
				// 8011, 1000, "password");
				// Jedis jedis = pool.getResource();
				//
				// jedis.set("SENDER_IP", ip);
				// String value = jedis.get("SENDER_IP");
				// System.out.println(value);
				// // assertTrue( value != null && value.equals("bar") );
				//
				// // assertTrue( value == null );
				//
				// if (jedis != null) {
				// jedis.close();
				// }

				InputStream is = connection.getInputStream(); // Stream
				InputStreamReader isr = new InputStreamReader(is); // char
				br = new BufferedReader(isr); // String

				OutputStream os = connection.getOutputStream(); // Stream
				OutputStreamWriter osw = new OutputStreamWriter(os); // char
				pw = new PrintWriter(osw); // String

				String msg = null;
				while ((msg = br.readLine()) != null) { // main쓰레드.
					if (msg.equals("quit"))
						break; // "quit"입력시 종료.

					System.out.println(msg);

					pw.println(msg);
					pw.flush();

				}

			} catch (IOException e) {
				System.out.println("Server setting error" + e.getMessage());
			} finally {
				try {
					if (pw != null)
						pw.close();
					if (br != null)
						br.close();
					if (connection != null)
						connection.close();
				} catch (Exception e) {
				}
			}
		}

	}

	public static void main(String[] args) {
		TestServer testServer = new TestServer(9999);
		
		try {
			testServer.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}


