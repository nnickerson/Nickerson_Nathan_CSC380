package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
	//Second attempt for server//
	ServerSocket myServerSocket;
	
	public Server() {
		createServerSocket();
		runServer();
	}
	
	public void createServerSocket() {
		try {
			myServerSocket = new ServerSocket(7823);
		}
		catch(Exception e) {
			System.out.println("The server could not setup a server socket - " + e.getMessage());
		}
	}
	
	public void runServer() {
		boolean isRunning = true;
		while(isRunning) {
			try {
				Socket newSocket = myServerSocket.accept();
				ServerTask serverTask = new ServerTask(newSocket);
				serverTask.start();
			} catch (IOException e) {
				System.out.println("A new socket for an incoming client could not be created - " + e.getMessage());
			}
		}
	}
	//End of second server attempt//
}
