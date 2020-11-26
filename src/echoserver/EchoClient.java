package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {
		Socket socket = new Socket("127.0.0.1", PORT_NUMBER);
		InputStream socketInputStream = socket.getInputStream();
		OutputStream socketOutputStream = socket.getOutputStream();

		OutputWriter writer = new OutputWriter(socket, socketOutputStream);
		InputReader reader = new InputReader(socket, socketInputStream);

		Thread inputReader = new Thread(writer);
		Thread outputWriter = new Thread(reader);
		inputReader.start();
		outputWriter.start();
	}

	public class InputReader implements Runnable{
		Socket socket;
		InputStream input;

		public InputReader(Socket sock, InputStream stream) {
			socket = sock;
			input = stream;
		}

		@Override
		public void run() {
			try {
				//recieve server data
				int inputbytes;
				while ((inputbytes = input.read()) != -1) {
					//System.out.println("Hey! inputbytes is holding this: " + inputbytes);
					System.out.write(inputbytes);
				}

				//flush any leftovers
				System.out.flush();

				// Close the socket when we're done reading from it
				socket.close();

				// Provide some minimal error handling.
			} catch (IOException e) {
				System.out.println("We were unable to get the inputstream stream.");
				System.out.println("You should probably go check that out.");
			}
		}
		}

	public class OutputWriter implements Runnable {
		OutputStream output;
		Socket socket;
		public OutputWriter(Socket sock, OutputStream stream) {
			socket = sock;
			output = stream;
		}

		int inputbytes;
		@Override
		public void run() {
			try{
				while((inputbytes = System.in.read()) != -1){
					output.write(inputbytes);
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
