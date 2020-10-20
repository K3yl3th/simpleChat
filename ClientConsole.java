// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        
        if (message != null && message.startsWith("#")) 
        {
        	if (message.length() > 1) {        		
        		String command = message.substring(1).split(" ")[0];
        		
        		switch (command) {
        		
	        		case "quit":
	        			
	        			display("Closing program.");
	        			client.quit();
	        			break;
	        			
	        		case "logoff":
	        			
	        			client.closeConnection();
	        			break;
	        			
	        		case "sethost":
	        			
	        			if (client.isConnected()) {
	        				display("ERROR: Close the client before changing hosts.");
	        			}
	        			else if (message.split(" ").length != 2) {
	        				display("ERROR: Wrong command format. Use #sethost <host>.");
	        			}
	        			else {
	        				String host = message.split(" ")[1];
	        				display(String.format("Host is now %s.", host));
	        				client.setHost(host);
	        			}
	        			break;
	        			
	        		case "setport":
	        			
	        			if (client.isConnected()) {
	        				display("ERROR: Close the client before changing ports.");
	        			}
	        			else if (message.split(" ").length != 2) {
	        				display("ERROR: Wrong command format. Use #setport <port>.");
	        			}
	        			else {
	        				try {
		        				int port = Integer.parseInt(message.split(" ")[1]);
		        				display(String.format("Port is now %d.", port));
		        				client.setPort(port);
	        				}
	        				catch (NumberFormatException e) {
	        					display("ERROR: Wrong port format. Please enter an integer as port.");
	        				}
	        			}
	        			break;
	        			
	        		case "login":
	        			
	        			if (client.isConnected()) {
	        				display("ERROR: Client already connected.");
	        			}
	        			else {
	        				client.openConnection();
	        			}
	        			break;
	        			
	        		case "gethost":
	        			
	        			display(String.format("Host: %s", client.getHost()));
	        			break;
	        			
	        		case "getport":
	        			
	        			display(String.format("Port: %d", client.getPort()));
	        			break;
	        			
	        		default:
	        			
	        			display("Did not recognize the command");
	        			break;
        		}
        	}
        }
        else {        	
        	client.handleMessageFromClientUI(message);
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "localhost";
    int port = DEFAULT_PORT;
    
    if (args.length > 0) {
    	host = args[0];
    }
    
    if (args.length > 1) {
    	try {
    		port = Integer.parseInt(args[1]);
    	}
    	catch (NumberFormatException e) {
    		System.out.println(String.format("Wrong port format. Using default port %s.", DEFAULT_PORT));
    	}
    }
    
    ClientConsole chat= new ClientConsole(host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
