package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
	
	public static final int PORT_NUMBER = 6013; 
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
			ExecutorService executor = Executors.newFixedThreadPool(10);

			while (true) {
				Socket socket = serverSocket.accept();

				System.out.println("Got a request!");

				executor.execute(new Runnable() {
					public void run() {
						//Input from the client
						InputStream input = socket.getInputStream();

						// Output to send to the client
						OutputStream output = socket.getOutputStream();
						int inputByte;
					
						while ((inputByte = input.read()) != -1) {
						  output.flush();
						  output.write(inputByte);
						}
					}
				});
			
				// Close the client socket since we're done.
				executor.shutdown();
				socket.close();
		  	}
		} catch (IOException ioe) {
			System.out.println("We caught an unexpected exception");
      		System.err.println(ioe);
		}
	}
}