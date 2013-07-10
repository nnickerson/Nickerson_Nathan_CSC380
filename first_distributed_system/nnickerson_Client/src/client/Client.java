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
	
	public void prompt() {
		System.out.println("Here are your options:\nEnter 1 to ADD\nEnter 2 to SUBTRACT");
		String userInput = "";
		int choice = 0;
		try {
			userInput = scanner.nextLine();
			choice = Integer.parseInt(userInput);
			pickNumbers(choice);
		}
		catch(Exception e) {
			System.out.println("Your choice was invalid. \nPlease enter the number of your choice and press the return key.");
			prompt();
		}
	}
	
	public void pickNumbers(int userChoice) {
		if(userChoice == 1) {
			try {
				System.out.println("Please enter your first number to add:");
				int num1 = Integer.parseInt(scanner.nextLine());
				
				System.out.println("Please enter your second number to add:");
				int num2 = Integer.parseInt(scanner.nextLine());
				
				sendToServer(num1, num2, userChoice);
			}
			catch(Exception e) {
				System.out.println("You've entered an invalid response. Please try again.");
				pickNumbers(1);
			}
		}
		else if(userChoice == 2) {
			try {
				System.out.println("Please enter your first number to subtract:");
				int num1 = Integer.parseInt(scanner.nextLine());
				
				System.out.println("Please enter your second number to subtract:");
				int num2 = Integer.parseInt(scanner.nextLine());
				
				sendToServer(num1, num2, userChoice);
			}
			catch(Exception e) {
				System.out.println("You've entered an invalid response. Please try again.");
				pickNumbers(2);
			}
		}
	}
	
	public void sendToServer(int num1, int num2, int userChoice) {
		try {
			OutputStream os = cSocket.getOutputStream();
			PrintWriter osw = new PrintWriter(os);
			System.out.println("Creating the message from the client to send to the server.");
//			char[] message = new char[3];
//			message[0] = (""  + num1).toCharArray()[0];
//			message[1] = (""  + num2).toCharArray()[0];
//			message[2] = (""  + userChoice).toCharArray()[0];
			osw.write("" + num1 + "" + num2 + "" + userChoice);
			System.out.println("Client is writing message.");
			osw.flush();
			cSocket.shutdownOutput();
			System.out.println("Client has sent the message.");
			System.out.println("This client is waiting for a response from the server.");
			
			boolean receivedResponse = false;
			while(!receivedResponse) {
				if(cSocket != null) {
					InputStream is = cSocket.getInputStream();
					InputStreamReader clientStream = new InputStreamReader(is);
					BufferedReader buffRead = new BufferedReader(clientStream);
					System.out.println(buffRead.readLine());
					cSocket.close();
					receivedResponse = true;
				}
			}
			
			System.out.println("The client is now closed!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
