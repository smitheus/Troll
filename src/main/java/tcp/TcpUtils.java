package tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpUtils
{
	// server functionality
	
	SocketObject serverObject ;
	
	public TcpUtils(int port)
	{
		serverObject = new SocketObject(port) ;
		
		serverObject.name = "LogTrollServer" ;
	}

	public boolean startUp ()
	{
		// spawn a thread to act as the server and accept one connection

		ServerThread serverThread = new ServerThread (serverObject);

		serverThread.start ();

		return serverObject.waitForServerToStart () ;
	}
	
	public void run()
	{
		while (true)
		{
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// client functionality
	
	SocketObject clientObject ;
	
	public TcpUtils (String server, int port)
	{
		clientObject = new SocketObject (server, port) ;
	}
	
	public boolean connect (String source)
	{
		clientObject.name = source ;
		
		try
		{
			System.out.println(clientObject.getLogPrefix() + " : Trying to connect to server at : " + clientObject.hostname + " " + clientObject.portNumber);
			
			clientObject.connectionSocket = new Socket(clientObject.hostname, clientObject.portNumber);
			
			// we don't expect the server to send regularly so set an infinite timeout
			clientObject.connectionSocket.setSoTimeout(0) ;
			
			// get the input readers
			clientObject.inputReader = new InputStreamReader (clientObject.connectionSocket.getInputStream()) ;
			clientObject.bufferedReader = new BufferedReader(clientObject.inputReader) ;
			
			clientObject.outputStream = new DataOutputStream(clientObject.connectionSocket.getOutputStream());
			
			System.out.println(clientObject.getLogPrefix() + " : Connected as client");					
		}
		catch (Exception ex)
		{
			System.out.println(clientObject.getLogPrefix() + " : Exception = " + ex.getMessage() ) ;
			
			return false  ;
		}
		
		return true ;
	}
	
	public boolean send (String msg)
	{
		return send (msg, clientObject) ;
	}
	
	public boolean send (String msg, SocketObject socketObject)
	{
		boolean messageSent = false ;
		
		try
		{
			System.out.println(socketObject.getLogPrefix() + " : Writing message");
			
			socketObject.outputStream.writeBytes (msg) ;
			
			messageSent = true ;
		}
		catch (IOException ex)
		{
			System.out.println(socketObject.getLogPrefix() + " : Exception : " + ex.getMessage() ) ;			
		}
		
		return messageSent ;
	}
	

	public String readLine()
	{
		try
		{
			return clientObject.bufferedReader.readLine() ;
		}
		catch (IOException e)
		{
			// TODO have we been disconnected?
			e.printStackTrace();
		}
		
		return null ;
	}
	
	// utility classes
	
	private class SocketObject
	{
		String name ;
	
		public SocketObject(int port)
		{
			portNumber = port ;
		}

		public SocketObject (String server, int port)
		{
			hostname = server ;
			portNumber = port ;
		}
		
		public SocketObject(SocketObject serverObject)
		{
			this.name = serverObject.name ;
			this.hostname = serverObject.hostname  ;
			this.portNumber = serverObject.portNumber ;
		}

		public String getLogPrefix (String string)
		{
			return getLogPrefix() + string + ":" ;
		}
		
		public String getLogPrefix ()
		{
			return name ;
		}
	
		public boolean waitForServerToStart()
		{
			try
	    	  {
	    		  while ( ! serverStarted)
			      {
	    			  if (serverFailedToStart)
	    				  return false ;
	    			  
	    			  Thread.sleep (1000) ;
	    			  
	    			  //System.out.println("*");
			      }
	    	  }
	    	  catch (Exception ex)
	    	  {
	    		  return false ;
	    	  }
			
			return true ;
		}
		
		String hostname ;
		int portNumber ;
		
		// server side
		boolean serverStarted ;
		boolean serverFailedToStart ;
		
		ServerSocket serverSocket ;

		Socket connectionSocket;
		
		// input mode flag
		
		boolean lineReading = true ;
		
		BufferedReader bufferedReader ;
		InputStreamReader inputReader ;
		
		//BufferedInputStream bufferedInputStream ;
		//InputStream inputStream ;	
		DataOutputStream outputStream ;
	}

	class ServerThread extends Thread
	{
		SocketObject socketObject ;

		ServerThread (SocketObject socketObject)
		{
			this.socketObject = socketObject ;
		}

		public void run ()
		{
			System.out.println(socketObject.getLogPrefix() + " : Opening port at [" + socketObject.hostname + "][" + socketObject.portNumber + "]");

			try
			{
				System.out.println(socketObject.getLogPrefix() + " : Creating server socket");

				socketObject.serverSocket = new ServerSocket(socketObject.portNumber);
				
				System.out.println(socketObject.getLogPrefix() + " : Waiting for connection");
				socketObject.serverStarted = true ;

				while (true)
				{
					SocketObject connectedClientObject = new SocketObject(socketObject);

					connectedClientObject.connectionSocket = socketObject.serverSocket
							.accept();

					System.out.println(socketObject.getLogPrefix() + " : Connection received");

					// since we await our client's pleasure to send something to the server, set an infinite timeout
					connectedClientObject.connectionSocket.setSoTimeout(0);
					
					// get the input readers
					connectedClientObject.inputReader = new InputStreamReader (connectedClientObject.connectionSocket.getInputStream()) ;
					connectedClientObject.bufferedReader = new BufferedReader(connectedClientObject.inputReader) ;
					
					connectedClientObject.outputStream = new DataOutputStream(connectedClientObject.connectionSocket.getOutputStream());
					
					ConnectedClientThread clientThread = new ConnectedClientThread (connectedClientObject);

					clientThread.start ();
				}
			}
			catch (Exception ex)
			{
				System.out.println(socketObject.getLogPrefix() + " : Exception during accept " + ex.getMessage());
				
				socketObject.serverFailedToStart = true ;
				
				System.exit(0) ;
			}
		}
	}
	
	class ConnectedClientThread extends Thread
	{
		SocketObject clientObject ;
		
		public ConnectedClientThread (SocketObject clientObject)
		{
			this.clientObject = clientObject ;
		}
		
		public void run ()
		{
			int count = 0 ;
			
			BufferedReader bufferedReader = clientObject.bufferedReader ;
			InputStreamReader inputReader = clientObject.inputReader ;
			
			while (true)
			{
				try
				{
					if (clientObject.lineReading)
					{
						String line = bufferedReader.readLine() ;
						if (line == null)
						{
							// null indicates that the client has closed the connection
							// so tidy up and terminate this thread
							
							System.out.println(clientObject.getLogPrefix() + " : Stopping the client thread");
							
							return ;
						}
						
						System.out.println(clientObject.getLogPrefix() + " : Received : " + line);
						
						// TODO decide how to parse the received line and possibly respond
						//send(clientObject.getLogPrefix() + ", ACK", clientObject) ;
					}
					else
					{
						System.out.println("Waiting for input");

						byte[] buffer = new byte[2048] ;

						count = inputReader.read() ;
						
						if (count > 0)
						{
							String part = new String(buffer).substring(0, count) ;
							System.out.println(clientObject.getLogPrefix() + " : Received " + count + " bytes [" + part + "]");
						}
						else
							return ;
					}
					
					// nothing to read, so have a short sleep
					
					try {
						Thread.sleep (50) ;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				catch (IOException ioEx)
				{
					System.out.println("IOEX during read " + ioEx.getMessage());
					
					return ;
				}
			}
		}
	}
}

