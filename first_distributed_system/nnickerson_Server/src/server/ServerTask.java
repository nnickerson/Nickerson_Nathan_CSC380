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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author nnickerson
 *
 */
public class ServerTask extends Thread {
	public Socket taskSocket;
	public Class pickedClass;
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
		String serverResponse = " ;These are the parameters you can use.;Please seperate parameters with commas and no spaces:;(int,int[] would be: 1,3,4,2,6)";
		String paramChoices = ";";
		for(int i = 0; i < params.length; i++) {
			paramChoices += params[i].getSimpleName() + ",";
		}
		serverResponse += paramChoices;
		pw.println(serverResponse);
		pw.flush();
		System.out.println("Sent a response back to the client with params.");
		
		try {
			String clientParams = br.readLine();
			useClientParams(methodChosen, clientParams, pw, br, paramChoices);
		}
		catch(Exception e) {
			System.out.println("There was an error getting the clients parameters - " + e.getMessage());
		}
	}
	
	public void useClientParams(Method methodChosen, String clientParams, PrintWriter pw, BufferedReader br, String methodParams) {
		String[] clientsParams = clientParams.split(",");
		String[] paramTypesToFollow = methodParams.split(",");
		boolean hasMultipleParams = false;
		String singleType = "";
		String result = "";
		
		if(paramTypesToFollow.length == 1) {
			if(paramTypesToFollow[0].contains("[]")) {
				hasMultipleParams = true;
			}
		}
		else {
			if(paramTypesToFollow[paramTypesToFollow.length-1].contains("[]")) {
				hasMultipleParams = true;
			}
		}
		result = "" + invokeMethod(methodChosen, clientsParams, paramTypesToFollow, hasMultipleParams);
				
		System.out.println("Server calculation is " + result);
		pw.println("Your answer is " + result);
		pw.flush();
	}
	
	public String invokeMethod(Method methodChosen, String[] clientsParams, String[] paramTypesToFollow, boolean hasMultipleParams) {
		String result = "";
		
		if(paramTypesToFollow.length == 1 && hasMultipleParams) {
			if(paramTypesToFollow[0].contains("double")) {
				System.out.println("INVOKIGN WITH DOUBLES.");
				double[] doubleArgs = new double[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					doubleArgs[i] = Double.parseDouble(clientsParams[i]);
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), double[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), doubleArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("int")) {
				System.out.println("INVOKING WITH INTS.");
				int[] intArgs = new int[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					intArgs[i] = Integer.parseInt(clientsParams[i]);
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), int[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), intArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("boolean")) {
				System.out.println("INVOKING WITH BOOLEANS.");
				boolean[] booleanArgs = new boolean[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					booleanArgs[i] = Boolean.parseBoolean(clientsParams[i]);
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), boolean[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), booleanArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("byte")) {
				System.out.println("INVOKING WITH BYTES.");
				byte[] byteArgs = new byte[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					byteArgs[i] = Byte.parseByte(clientsParams[i]);
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), byte[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), byteArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("short")) {
				System.out.println("INVOKING WITH SHORTS.");
				short[] shortArgs = new short[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					shortArgs[i] = Short.parseShort(clientsParams[i]);
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), short[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), shortArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("float")) {
				System.out.println("INVOKING WITH FLOATS.");
				float[] floatArgs = new float[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					floatArgs[i] = Float.parseFloat(clientsParams[i]);
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(),float[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), floatArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("long")) {
				System.out.println("INVOKING WITH LONGS.");
				long[] longArgs = new long[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					longArgs[i] = Long.parseLong(clientsParams[i]);
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), long[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), longArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("String")) {
				System.out.println("INVOKING WITH INTS.");
				String[] stringArgs = new String[clientsParams.length];
				for(int i = 0; i < clientsParams.length; i ++) {
					stringArgs[i] = clientsParams[i];
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), String[].class);
					result = "" + meth.invoke(pickedClass.newInstance(), stringArgs);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		else if(paramTypesToFollow.length == 1 && !hasMultipleParams) {
			if(paramTypesToFollow[0].contains("double")) {
				System.out.println("INVOKIGN WITH DOUBLE.");
				double doubleArg = Double.parseDouble(clientsParams[0]);
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), double.class);
					result = "" + meth.invoke(pickedClass.newInstance(), doubleArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("int")) {
				System.out.println("INVOKING WITH INT.");
				int intArg = Integer.parseInt(clientsParams[0]);
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), int.class);
					result = "" + meth.invoke(pickedClass.newInstance(), intArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("boolean")) {
				System.out.println("INVOKING WITH BOOLEAN.");
				boolean booleanArg = Boolean.parseBoolean(clientsParams[0]);			
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), boolean.class);
					result = "" + meth.invoke(pickedClass.newInstance(), booleanArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("byte")) {
				System.out.println("INVOKING WITH BYTE.");
				byte byteArg = Byte.parseByte(clientsParams[0]);					
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), byte.class);
					result = "" + meth.invoke(pickedClass.newInstance(), byteArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("short")) {
				System.out.println("INVOKING WITH SHORT.");
				short shortArg = Short.parseShort(clientsParams[0]);
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), short.class);
					result = "" + meth.invoke(pickedClass.newInstance(), shortArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("float")) {
				System.out.println("INVOKING WITH FLOAT.");
				float floatArg = Float.parseFloat(clientsParams[0]);
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(),float.class);
					result = "" + meth.invoke(pickedClass.newInstance(), floatArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("long")) {
				System.out.println("INVOKING WITH LONG.");
				long longArg = Long.parseLong(clientsParams[0]);
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), long.class);
					result = "" + meth.invoke(pickedClass.newInstance(), longArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			else if(paramTypesToFollow[0].contains("String")) {
				System.out.println("INVOKING WITH String.");
				String stringArg = clientsParams[0];
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), String.class);
					result = "" + meth.invoke(pickedClass.newInstance(), stringArg);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		else if(paramTypesToFollow.length > 1 && !hasMultipleParams) {
			Class[] prims = new Class[paramTypesToFollow.length]; 
			Object[] values = new Object[paramTypesToFollow.length];
			for(int i = 0; i < paramTypesToFollow.length; i++) {
				if(paramTypesToFollow[i].contains("int")) {
					prims[i] = int.class;
					values[i] = Integer.parseInt(clientsParams[i]);
				}
				else if(paramTypesToFollow[i].contains("String")) {
					prims[i] = String.class;
					values[i] = clientsParams[i];
				}
				else if(paramTypesToFollow[i].contains("long")) {
					prims[i] = long.class;
					values[i] = Long.parseLong(clientsParams[i]);
				}
				else if(paramTypesToFollow[i].contains("short")) {
					prims[i] = short.class;
					values[i] = Short.parseShort(clientsParams[i]);
				}
				else if(paramTypesToFollow[i].contains("double")) {
					prims[i] = double.class;
					values[i] = Double.parseDouble(clientsParams[i]);
				}
				else if(paramTypesToFollow[i].contains("boolean")) {
					prims[i] = boolean.class;
					values[i] = Boolean.parseBoolean(clientsParams[i]);
				}
				else if(paramTypesToFollow[i].contains("byte")) {
					prims[i] = byte.class;
					values[i] = Byte.parseByte(clientsParams[i]);
				}
				else if(paramTypesToFollow[i].contains("float")) {
					prims[i] = float.class;
					values[i] = Float.parseFloat(clientsParams[i]);
				}
			}
//			for(int i = 0; i < values.length; i++) {
//				values[i] = ()clientsParams[i];
//			}
			
			try {
				Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), prims);
				result = "" + meth.invoke(pickedClass.newInstance(), values);
			} catch (Exception e) {
				System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
				e.printStackTrace();
			}
		}
		else if(paramTypesToFollow.length > 1 && hasMultipleParams) {
			try {
				Class[] prims = new Class[paramTypesToFollow.length]; 
				Object[] values = new Object[paramTypesToFollow.length];
				for(int i = 0; i < paramTypesToFollow.length; i++) {
					if(paramTypesToFollow.length-1 > i) {
						if(paramTypesToFollow[i].contains("int")) {
							prims[i] = int.class;
							values[i] = Integer.parseInt(clientsParams[i]);
						}
						else if(paramTypesToFollow[i].contains("String")) {
							prims[i] = String.class;
							values[i] = clientsParams[i];
						}
						else if(paramTypesToFollow[i].contains("long")) {
							prims[i] = long.class;
							values[i] = Long.parseLong(clientsParams[i]);
						}
						else if(paramTypesToFollow[i].contains("short")) {
							prims[i] = short.class;
							values[i] = Short.parseShort(clientsParams[i]);
						}
						else if(paramTypesToFollow[i].contains("double")) {
							prims[i] = double.class;
							values[i] = Double.parseDouble(clientsParams[i]);
						}
						else if(paramTypesToFollow[i].contains("boolean")) {
							prims[i] = boolean.class;
							values[i] = Boolean.parseBoolean(clientsParams[i]);
						}
						else if(paramTypesToFollow[i].contains("byte")) {
							prims[i] = byte.class;
							values[i] = Byte.parseByte(clientsParams[i]);
						}
						else if(paramTypesToFollow[i].contains("float")) {
							prims[i] = float.class;
							values[i] = Float.parseFloat(clientsParams[i]);
						}
					}
					else {
						String type = paramTypesToFollow[i];
						int index = i;
						int newIndex = 0;
						if(type.contains("int")) {
							prims[i] = int[].class;
							int[] ints = new int[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								ints[newIndex] = Integer.parseInt(clientsParams[a]);
								newIndex++;
							}
							values[index] = ints;
						}
						else if(type.contains("String")) {
							prims[i] = String[].class;
							String[] strings = new String[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								strings[newIndex] = clientsParams[a];
								newIndex++;
							}
							values[index] = strings;
						}
						else if(type.contains("long")) {
							prims[i] = long[].class;
							long[] longs = new long[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								longs[newIndex] = Long.parseLong(clientsParams[a]);
								newIndex++;
							}
							values[index] = longs;
						}
						else if(type.contains("short")) {
							prims[i] = short[].class;
							short[] shorts = new short[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								shorts[newIndex] = Short.parseShort(clientsParams[a]);
								newIndex++;
							}
							values[index] = shorts;
						}
						else if(type.contains("double")) {
							prims[i] = double[].class;
							double[] doubles = new double[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								doubles[newIndex] = Double.parseDouble(clientsParams[a]);
								newIndex++;
							}
							values[index] = doubles;
						}
						else if(type.contains("boolean")) {
							prims[i] = boolean[].class;
							boolean[] booleans = new boolean[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								booleans[newIndex] = Boolean.parseBoolean(clientsParams[a]);
								newIndex++;
							}
							values[index] = booleans;
						}
						else if(type.contains("byte")) {
							prims[i] = byte[].class;
							byte[] bytes = new byte[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								bytes[newIndex] = Byte.parseByte(clientsParams[a]);
								newIndex++;
							}
							values[index] = bytes;
						}
						else if(type.contains("float")) {
							prims[i] = float[].class;
							float[] floats = new float[(clientsParams.length)-i];
							for(int a = i; a < clientsParams.length; a++) {
								floats[newIndex] = Float.parseFloat(clientsParams[a]);
								newIndex++;
							}
							values[index] = floats;
						}
						
					}
				}
				
				try {
					Method meth = pickedClass.getDeclaredMethod(methodChosen.getName(), prims);
					result = "" + meth.invoke(pickedClass.newInstance(), values);
				} catch (Exception e) {
					System.out.println("PROBLEM INVOKING THE METHOD! " + e.getMessage());
					e.printStackTrace();
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
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
