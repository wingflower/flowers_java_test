package sender;

public interface Sender {
	void 	setDelimiter();		// Define a delimiter
	boolean checkConnection();	// Check a connection to server 
	Object 	readLog();			// Read logs
	void 	sendLog();			// Send logs
}
