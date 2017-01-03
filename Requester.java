package ie.gmit.sw;

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	String ipaddress;
 	Scanner user_input;
 	boolean authenticated;
	Requester()
	{
		user_input = new Scanner(System.in);
		ipaddress = "54.244.76.23";
		Client userInfo = new Client();
		authenticated = false;
	}
	void run()
	{
		try{
			//1. creating a socket to connect to the server
			
			requestSocket = new Socket(ipaddress, 2004);
			System.out.println("Connected to "+ipaddress+" in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server
			do{
				try{

					message = (String)in.readObject();
					System.out.println(message);
					while(authenticated ==false){
					authenticated = startMenu();
					}
					
					if (authenticated == true){
							System.out.println("User is authenticated");
							//show menu
					}
					
					
				}
				catch(ClassNotFoundException classNot){
					System.err.println("data received in unknown format");
				}
			}while(!message.equals("Quit!"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				System.out.println("Now closing connection");
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	boolean startMenu() throws ClassNotFoundException, IOException{
		message = (String)in.readObject();
		System.out.println(message);
		message = user_input.next();
		sendMessage(message);
		
		System.out.println("Listening for response");
		
		message = (String)in.readObject();
		System.out.println(message);
		
		switch(message){
		case "Login":
			System.out.println("In Login");
			message = (String)in.readObject();
			System.out.println(message);
			user_input.nextLine();
			message = user_input.nextLine();
			sendMessage(message);
			//address
			message = (String)in.readObject();
			System.out.println(message);
			message = user_input.nextLine();
			sendMessage(message);
			
			
			message = (String)in.readObject();
			if(message.equals("Authenticated")){
				authenticated = true;
				System.out.println("Authentication = "+authenticated);
				return true;
			}
			else{
				System.out.println("Failed to authenticate");
			}
			
			break;
			
		case "Register":
			System.out.println("In register \n");
			
			registerUser();
			break;
			
		case "Quit!":
			System.out.println("In quit");
			System.out.println("Now closing connection");
			sendMessage("bye");
			System.exit(0);
			break;
		default :
			
		}
		return authenticated = false;
		
	}
	private void registerUser() throws IOException, ClassNotFoundException {
		//name
		message = (String)in.readObject();
		System.out.println(message);
		user_input.nextLine();
		message = user_input.nextLine();
		sendMessage(message);
		//address
		message = (String)in.readObject();
		System.out.println(message);
		message = user_input.nextLine();
		sendMessage(message);
		//account number
		message = (String)in.readObject();
		System.out.println(message);
		message = user_input.nextLine();
		sendMessage(message);
		//username
		message = (String)in.readObject();
		System.out.println(message);
		message = user_input.nextLine();
		sendMessage(message);
		//password
		message = (String)in.readObject();
		System.out.println(message);
		message = user_input.nextLine();
		sendMessage(message);
		
		
		
		//confirmation message
		message = (String)in.readObject();
		System.out.println(message);
	}
	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
}