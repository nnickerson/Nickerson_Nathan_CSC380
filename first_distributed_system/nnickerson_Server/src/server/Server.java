package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	Socket sSocket;
	String serverResponse = "";
	ServerSocket sServerSocket;
	Thread clientConnection;

	public Server() {
		
	}

	public void setupServer() {
		try {
			sServerSocket = new ServerSocket(7823);
			while(true) {
			boolean isRunning = true;
			sSocket = sServerSocket.accept();
			
			if(sSocket.isConnected()) {
			clientConnection = new Thread() {
				public void run() {
					try {
						System.out.println("Server has started!");
						System.out.println("A client has connected!");
						
							if(sSocket != null) {
								InputStream is = sSocket.getInputStream();
								InputStreamReader clientStream = new InputStreamReader(is);
								BufferedReader buffRead = new BufferedReader(clientStream);
									String clientRequest = buffRead.readLine();
									
									int choice = Integer.parseInt("" + clientRequest.toCharArray()[2]);
									
									MathLogic logic = new MathLogic();
									if(choice == 1) {
										System.out.println(logic.add(Integer.parseInt("" +clientRequest.toCharArray()[0]), Integer.parseInt("" + clientRequest.toCharArray()[1])));
										serverResponse = "Your addition summed to " + logic.add(Integer.parseInt("" +clientRequest.toCharArray()[0]), Integer.parseInt("" + clientRequest.toCharArray()[1]));
										
										//To client now//
										OutputStream os = sSocket.getOutputStream();
										PrintWriter osw = new PrintWriter(os);
										System.out.println("Creating the message from the server to send to the client.");
										osw.write(serverResponse);
										System.out.println("Server is writing message.");
										osw.flush();
										sSocket.shutdownOutput();
										//Done sending to client//
										
//										isRunning = false;
									}
									else if(choice == 2) {
										System.out.println(logic.subtract(Integer.parseInt("" +clientRequest.toCharArray()[0]), Integer.parseInt("" + clientRequest.toCharArray()[1])));
										serverResponse = "The answer for the subtraction is " + logic.subtract(Integer.parseInt("" +clientRequest.toCharArray()[0]), Integer.parseInt("" + clientRequest.toCharArray()[1]));
										
										//To client now//
										OutputStream os = sSocket.getOutputStream();
										PrintWriter osw = new PrintWriter(os);
										System.out.println("Creating the message from the server to send to the client.");
										osw.write(serverResponse);
										System.out.println("Server is writing message.");
										osw.flush();
										sSocket.shutdownOutput();
										//Done sending to client//
										
//										isRunning = false;
									}
							}
						
						System.out.println("The server has Written the message!");
					}
						
					catch(Exception e) {
						System.out.println(e.getMessage() + " - A client has disconnected.");
					}
				}
			};
			
			clientConnection.start();
			}
			
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
