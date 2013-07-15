package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Client {

	Socket cSocket;
	Scanner scanner = new Scanner(System.in);
	
	public Client() {
		try {
			cSocket = new Socket("localhost", 7823);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clientRun() {
		boolean receivedResponse = false;
		while(!receivedResponse) {
			if(cSocket != null) {
				String serverMessage = getServerMessage();
				if(serverMessage != null) {
					if(serverMessage.equalsIgnoreCase("quit")) {
						try {
							cSocket.close();
						}
						catch(Exception e) {
							System.out.println("There was an error closing the clients connection!");
						}
						receivedResponse = true;
					}
					else {
						for(String s : serverMessage.split(";")) {
							System.out.println(s);
						}
					}
					sendToServer(getClientInput());
				}
			}
		}
		
		System.out.println("The client is now closed!");
	}
	
	public String getClientInput() {
		String stringedChoice = "";
		Scanner scan = new Scanner(System.in);
		return stringedChoice = scan.nextLine();
	}
	
	public String getServerMessage() {
		String serverMessage = "";
		try {
			InputStream is = cSocket.getInputStream();
			InputStreamReader clientStream = new InputStreamReader(is);
			BufferedReader buffRead = new BufferedReader(clientStream);
			serverMessage = buffRead.readLine();
		}
		catch(Exception e) {
			System.out.println("The client could not read in the server message!");
		}
		return serverMessage;
	}
	
	public void sendToServer(String clientMessage) {
		try {
			OutputStream os = cSocket.getOutputStream();
			PrintWriter osw = new PrintWriter(os);
			System.out.println("Creating the message from the client to send to the server.");
			osw.println(clientMessage);
			System.out.println("Client is writing message.");
			osw.flush();
			System.out.println("Client has sent the message.");
			System.out.println("This client is waiting for a response from the server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
