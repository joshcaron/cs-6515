import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class EchoServer {
	static Socket client;
	static InputStream serverStream;
	static PrintStream outputStream;
	static InetAddress HOST;
	static final Integer PORT = 5432;
	static final String IP = "129.10.218.138";
	
	public static void main(String[] args) {
		try {
			System.out.println("Creating client");
			initServers();
			System.out.println("Succeeded. Connected to server.");
			
			BufferedReader serverReader = new BufferedReader(new InputStreamReader(serverStream));
			Scanner serverOutput = new Scanner(serverReader);
			
			BufferedReader clientReader = new BufferedReader(new InputStreamReader(System.in));
			String input = "";
			while (true) {
				input = clientReader.readLine();
				outputStream.println(input);
				
				System.out.println(serverReader.readLine());
			}
			
			
//			do {
//				in = stdin.nextLine();
//				output.println(in);
//				out = reader.readLine();
//				System.out.println(out);
//			} while(!out.equals("bye"));
		} catch (IOException e) {
			System.out.println("Terminating.");
			System.exit(1);
		} finally {
//			try {
//				input.close();
//				output.close();
//				client.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}

	}

	static void initServers() throws IOException {
		try {
			HOST = InetAddress.getByName(IP);
		} catch (UnknownHostException e) {
			System.out.println("Error getting Host");
			return;
		}
		
		try {
			// Client calls accept on the server to create a connection
			// alternatively use client = new Socket(ip-address, port#)
			client = new Socket("localhost", PORT);
		} catch (IOException e) {
			System.out.println("Error creating client");
			throw e;
		}
		
		try {
			serverStream = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			System.out.println("Error creating input stream");
		}
		
		try {
			outputStream = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			System.out.println("Error creating output stream");
		}
	}
}
