import java.io.IOException;
import java.util.Scanner;

import common.ChatIF;

public class ServerConsole implements ChatIF {

	  //Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	  //Instance variables **********************************************
	  
	  /**
	   * The instance of the server that created this ConsoleServer.
	   */
	  EchoServer server;
	  
	  
	  
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 

	  
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ServerConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
		  try {
		    server= new EchoServer(port, this);
		    
		    // Create scanner object to read from console
		    fromConsole = new Scanner(System.in); 
		  }
		  catch(IOException e) {
			  display("ERROR - Could not listen for clients!");
		  }
	  }

	  
	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the servers's message handler.
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

	        				server.stopListening();
	        				server.close();
	        				System.out.println("Closing program.");
	        				System.exit(0);
	        				break;
	        				
	        			case "stop":
	        				
	        				server.stopListening();
	        				break;
	        				
	        			case "close":
	        				
	        				server.stopListening();
	        				server.close();
	        				break;
	        			    
	        			case "setport":
		        			
		        			if (server.isListening()) {
		        				System.out.println("ERROR: Close the client before changing ports.");
		        			}
		        			else if (message.split(" ").length != 2) {
		        				System.out.println("ERROR: Wrong command format. Use #setport <port>.");
		        			}
		        			else {
		        				try {
			        				int port = Integer.parseInt(message.split(" ")[1]);
			        				System.out.println(String.format("Port set to: %d.", port));
			        				server.setPort(port);
		        				}
		        				catch (NumberFormatException e) {
		        					System.out.println("ERROR: Wrong port format. Please enter an integer as port.");
		        				}
		        			}
		        			break;
		        			
	        			case "start":
	        				
	        				if (server.isListening()) {
	        					System.out.println("ERROR: Server is already listening for connections.");
	        				}
	        				else {
	        					server.listen();
	        				}
	        				break;
	        				
	        			case "getport":
	        				
	        				System.out.println(String.format("Port: %d", server.getPort()));
	        				break;
	        		
		        		default:
		        			
		        			System.out.println("Did not recognize the command");
		        			break;
	        		}
	        	}
	        }
	        else {        	
	        	message = "SERVER MESSAGE: " + message;
	        	display(message);
	        	server.sendToAllClients(message);
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
	   * This method is responsible for the creation of the Server UI.
	   *
	   * @param args[0] The host to connect to.
	   */
	  public static void main(String[] args) 
	  {
	    int port = DEFAULT_PORT;
	    
	    if (args.length > 0) {
	    	try {
	    		port = Integer.parseInt(args[1]);
	    	}
	    	catch (NumberFormatException e) {
	    		System.out.println(String.format("Wrong port format. Using default port %s.", DEFAULT_PORT));
	    	}
	    }
	    
	    ServerConsole console = new ServerConsole(port);
	    console.accept();  //Wait for console data
	  }

}
