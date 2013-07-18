/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nnickerson
 *
 */
public class ServerTask extends Thread {
	public Socket taskSocket;
	public Class pickedClass;
	public Object pickedObject;
	public Method[] pickedClassMethods;
	public String absolutePath = "";

	public ServerTask(Socket socket) {
		taskSocket = socket;
		this.setName("ServerTaskThread");
	}
	
	public void run() {
		try {
			System.out.println("New client has connected to this server!");
			OutputStream os = taskSocket.getOutputStream();
			PrintWriter osw = new PrintWriter(os);
			
			InputStream is = taskSocket.getInputStream();
			InputStreamReader clientStream = new InputStreamReader(is);
			BufferedReader buffRead = new BufferedReader(clientStream);
			
			System.out.println("Retrieving class from the clients absolute input for server...");
			System.out.println("PLEASE PUT YOUR CUSTOM CLASSES INSIDE OF THE importClasses PACKAGE IN THE SERVER PROJECT!!!");
			System.out.println("Make sure the user folows the method parameters.");
			getPickedClass(osw, buffRead);
			getPickedClassMethods();
			System.out.println("Picked class retrieved successfully!");
			
			String clientMessage = "";
			String serverResponse = "";
			
			osw.println("Hello Client! Enter 1 to explore. Enter 2 to quit.");
			osw.flush();
			
			boolean isCommunicating = true;
			while(isCommunicating) {
				clientMessage = buffRead.readLine();
				if(clientMessage != null) {
					if(clientMessage.equalsIgnoreCase("1")) {
						System.out.println("The client is now exploring this server!");
						exploreOption(osw, buffRead);
					}
					else if(clientMessage.equals("2")) {
						System.out.println("Client Terminated...");
						osw.close();
						buffRead.close();
						taskSocket.close();
						isCommunicating = false;
						System.exit(-1);
					}
					else {
						System.out.println("Your response was invalid. Please enter 1 to explore and 2 to quit.");
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println("There was an error on this server - " + e.getMessage());
		}
	}
	
	public void exploreOption(PrintWriter pw, BufferedReader br) {
		String serverResponse = "";
		serverResponse += "There are " + pickedClassMethods.length + " methods in the picked class.";
		serverResponse += ";Please enter the number corresponding to the method you'd like: ";
		System.out.println("After the response back to client.");
		for(int i = 0; i < pickedClassMethods.length; i++) {
			serverResponse += ";" + i + ": " + pickedClassMethods[i].getName();
		}
		pw.println(serverResponse);
		pw.flush();
		
		try {
			String clientResponse = br.readLine();
			getMethodParameters(Integer.parseInt(clientResponse), pw, br);
		}
		catch(Exception e) {
			System.out.println("The client has responded with an invalid option. Making them try again...");
			pw.println("You've entered an invalid response! Please try again!");
			pw.flush();
			exploreOption(pw, br);
		}
	}
	
	public void getMethodParameters(int choice, PrintWriter pw, BufferedReader br) {
		System.out.println("The client has chosen a method.");
		Method methodChosen = pickedClassMethods[choice];
		Class[] params = pickedClassMethods[choice].getParameterTypes();
		String serverResponse = " ;These are the parameters you can use.;Please seperate parameters with commas and no spaces:;(int,int,boolean would be: 1,3,false);Actual Params: ";
		String paramChoices = ";";
		
		List<String> notPrimTypes = new ArrayList<String>();
		boolean foundPrim = false;
			for(int i = 0; i < params.length; i++) {
				if(params[i].getSimpleName().contains("int") || params[i].getSimpleName().contains("String") || params[i].getSimpleName().contains("double") || params[i].getSimpleName().contains("float") || params[i].getSimpleName().contains("short") || params[i].getSimpleName().contains("long") || params[i].getSimpleName().contains("byte") || params[i].getSimpleName().contains("boolean")) {
					paramChoices += params[i].getSimpleName() + ",";
					serverResponse += params[i].getSimpleName() + ",";
					foundPrim = true;
				}
				else {
					notPrimTypes.add(params[i].getSimpleName());
				}
			}
		if(foundPrim) {
			serverResponse += paramChoices;
			pw.println(serverResponse);
			pw.flush();
			System.out.println("Sent a response back to the client with params.");
		}
		try {
			String clientParams = "";
			if(foundPrim) {
				clientParams = br.readLine();
			}
			serverResponse = "Please enter the number for the constructor you want to use for the following objects (list numbers separated by commas): ";
			for(int i = 0; i < notPrimTypes.size(); i++) {
				System.out.println("NOTPRIMMMMM: " + notPrimTypes.get(i));
				Constructor[] cntrs = Class.forName("importClasses." + notPrimTypes.get(i)).getConstructors();
				serverResponse+=notPrimTypes.get(i)+": ";
				for(int j = 0; j < Class.forName("importClasses." + notPrimTypes.get(i)).getConstructors().length; j++) {
					serverResponse +=";" + j + ": " + cntrs[j].getName();
				}
			}
			
			pw.println(serverResponse);
			pw.flush();
			
			String clientConstructRequest = br.readLine();
			List<Constructor> cs = new ArrayList<Constructor>();
			List<String> paramTypes = new ArrayList<String>();
			List<String> csCD = new ArrayList<String>();
			String[] requests = clientConstructRequest.split(",");
			for(int i = 0; i < notPrimTypes.size(); i++) {
				cs.add(Class.forName("importClasses." + notPrimTypes.get(i)).getConstructors()[Integer.parseInt(requests[i])]);
				serverResponse = "Please enter the values for the following parameters for the constructor " + cs.get(i).getName() + ":;";
				for(Class c : cs.get(i).getParameterTypes()) {
					serverResponse += c.getName() + ",";
				}
				pw.println(serverResponse);
				pw.flush();
				
				String csCDrequest = br.readLine();
				csCD.add(csCDrequest);
			}
			List<Object> objectParams = new ArrayList<Object>();
			for(int i = 0; i < cs.size(); i++) {
				for(Class c:cs.get(i).getParameterTypes()){
					paramTypes.add(c.getName());
				}
				String[] pts = new String[paramTypes.size()];
				for(int s=0;s<paramTypes.size();s++){
					pts[s]=paramTypes.get(s);
				}
				objectParams.add(cs.get(i).newInstance(getArguments(csCD.get(i),pts)));
			}
			
			useClientParams(methodChosen, clientParams, pw, br, paramChoices,objectParams);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	} 
	
	private Object[] getArguments(String string,String[] pt) {
		List<Object> params = new ArrayList<Object>();
		String[] processStrings = string.split(",");
		for(int i=0;i<pt.length;i++){
							if(pt[i].toString().contains("int")) {
									params.add(Integer.parseInt(processStrings[i]));
								}
								else if(pt[i].toString().contains("String")) {
									params.add(processStrings[i]);
								}
								else if(pt[i].toString().contains("long")) {
									params.add(Long.parseLong(processStrings[i]));
								}
								else if(pt[i].toString().contains("short")) {
									params.add(Short.parseShort(processStrings[i]));
								}
								else if(pt[i].toString().contains("double")) {
									params.add(Double.parseDouble(processStrings[i]));
								}
								else if(pt[i].toString().contains("boolean")) {
									params.add(Boolean.parseBoolean(processStrings[i]));
								}
								else if(pt[i].toString().contains("byte")) {
									params.add(Byte.parseByte(processStrings[i]));
								}
								else if(pt[i].toString().contains("float")) {
									params.add(Float.parseFloat(processStrings[i]));
								}

		}
		return params.toArray();
	}

	public void useClientParams(Method methodChosen, String clientParams, PrintWriter pw, BufferedReader br, String methodParams, List<Object>objectParams) {
		String[] paramTypesToFollow = methodParams.split(",");
		List<String> array = new ArrayList<String>();
		for(int i = 0; i < paramTypesToFollow.length; i++) {
			if(paramTypesToFollow[i].contains("int") || paramTypesToFollow[i].contains("String") || paramTypesToFollow[i].contains("double") || paramTypesToFollow[i].contains("float") || paramTypesToFollow[i].contains("short") || paramTypesToFollow[i].contains("long") || paramTypesToFollow[i].contains("byte") || paramTypesToFollow[i].contains("boolean")) {
				array.add(paramTypesToFollow[i]);
			}
		}
		String[] ar = new String[array.size()];
		for(int s=0;s<array.size();s++){
			ar[s]=array.get(s);
		}
		Object[] params = getArguments(clientParams, ar);
		String singleType = "";
		String result = "";
		
		
		result = "" + invokeMethod(methodChosen, params,objectParams.toArray(), paramTypesToFollow, pw, br);
				
		System.out.println("Server calculation is " + result);
		pw.println("Your answer is " + result);
		pw.flush();
	}
	
	public String invokeMethod(Method methodChosen, Object[] clientsParams,Object[] objectParams ,String[] paramTypesToFollow, PrintWriter pw, BufferedReader br) {
		String result = "";
		List<Object> primParams = new ArrayList<Object>();
		List<Object> notPrims = new ArrayList<Object>();
		for(Object o:objectParams){
			notPrims.add(o);
		}
		for(Object o:clientsParams){
			primParams.add(o);
		}
		List<Object> finalParams = new ArrayList<Object>();
		for(String i:paramTypesToFollow){
			if(i.contains("int") || i.contains("String") || i.contains("double") || i.contains("float") || i.contains("short") || i.contains("long") || i.contains("byte") || i.contains("boolean")){
				finalParams.add(primParams.remove(0));
			}
			else
			{
				finalParams.add(notPrims.remove(0));
			}
		}
		
		try {
			result = ""+methodChosen.invoke(pickedClass.newInstance(), finalParams.toArray());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void getPickedClass(PrintWriter pw, BufferedReader br) {
		try {
			File f = new File("src/importClasses");
			File[] files = f.listFiles();
			
			String fileChoices = "";
			
			fileChoices += "Please choose the class to use from the importClasses package:;";
			for(int i = 0; i < files.length; i++) {
				fileChoices += i + ": " + files[i].getName() + ";";
			}
			
			pw.println(fileChoices);
			pw.flush();
			
			String userInput = br.readLine();
			
			int choice = Integer.parseInt(userInput);
			
			File pc = files[choice];
			
			System.out.println("CLASSNAME:::: " + pc.getName().replaceAll(".java", ""));
			
			String className = pc.getName().replaceAll(".java", "");
			
			pickedClass = Class.forName("importClasses." + className);
			
		}
		catch(Exception e) {
			System.out.println("Could not find the class with the absolute path given - " + e.getMessage());
		}
	}
	
	public void getPickedClassMethods() {
		try {
			pickedClassMethods = pickedClass.getDeclaredMethods();
		}
		catch(Exception e) {
			System.out.println("Could not retrieve the methods from the absolute path picked - " + e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public ServerTask() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param target
	 */
	public ServerTask(Runnable target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public ServerTask(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 */
	public ServerTask(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param name
	 */
	public ServerTask(ThreadGroup group, String name) {
		super(group, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param target
	 * @param name
	 */
	public ServerTask(Runnable target, String name) {
		super(target, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 */
	public ServerTask(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 * @param stackSize
	 */
	public ServerTask(ThreadGroup group, Runnable target, String name,
			long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}

}
