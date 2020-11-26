package echoserver;

import org.omg.CosNaming.BindingListHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	// REPLACE WITH PORT PROVIDED BY THE INSTRUCTOR
	public static final int PORT_NUMBER = 6013;
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket sock = new ServerSocket(PORT_NUMBER);
		while(true){
			//wait until someone accepts
			Socket client = sock.accept();
			System.out.println("HEY! We got somebody!");
			//Input from the client
			InputStream in = client.getInputStream();
			// Output to send to the client
			OutputStream out = client.getOutputStream();

			ServerInstance instance = new ServerInstance(client, in, out);
			Thread newClient = new Thread(instance);

			newClient.start();
		}
	}

	public class ServerInstance implements Runnable {
		Socket client;
		InputStream input;
		OutputStream output;


		public ServerInstance(Socket client,InputStream in,OutputStream out){
			this.client = client;
			input = in;
			output = out;
		}

		@Override
		public void run() {
			try {
				while (true) {
					int inputByte;

					while ((inputByte = input.read()) != -1) {
						//System.out.println("Got a byte: " + inputByte);
						output.flush();
						output.write(inputByte);
					}

					// Close the client socket since we're done.
					client.close();
				}
			} catch (IOException e) {
				System.out.println("Take a look at ServerInstance, something went wrong.");
			}
		}
	}
}