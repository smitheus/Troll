package tcp;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class LogTroller
{
	// TODO need a config file
	
	static String server = "localhost" ;
	static int serverPort = 6789 ;
	
	static String source = "PAYEX" ;
	
	static int heartbeatPeriod = 5 ;
	
	static int lineBatchCount = 4 ;
	
	static LineReader lineReader = null ;
	static String loggerName ;
	
	public static void main(String[] args)
	{
		// to be read from config
		
		int startAtByte = 0 ;
		String pathStr = "/home/chris/workspace/LogTroller/src/main/resources" ;
		String nameStr = "samples.txt" ;
		
		// end of config
		
		loggerName = source + "_logger" ;
		
		String fileName = pathStr + "/" + nameStr ;
		
		File file = new File (fileName) ;
		lineReader = new LineReader(file, startAtByte) ;
		
		Path filePath = FileSystems.getDefault().getPath(pathStr, nameStr);
		
		try
		{
			BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
			
			System.out.println("ATTR " + attrs.fileKey());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// now loop on connection/disconnection
		
		while (true)
		{
			lineReader = new LineReader(file, startAtByte) ;
			
			run () ;
			
			System.out.println(loggerName + " : Disconnected from server");
			
			lineReader.close();
		}
	}
	
	private static void run ()
	{
		TcpUtils tcpClient = new TcpUtils(server, serverPort) ;
		
		while ( ! tcpClient.connect(loggerName) )
		{
			System.out.println(loggerName + " : Could not connect to server, sleeping before retry");
			
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				System.out.println(loggerName + " : Terminating on interrupt");
				
				System.exit(0);
			}
		}
		
		// announce who we are
		
		tcpClient.send(loggerName + ", NAME, " + source + "\n") ;
		
		// now start tailing
		
		try
		{
			boolean msgSent ;
			String msgToSend ;
            String line ;
            
            long nextHeartbeat = System.currentTimeMillis() + (heartbeatPeriod * 1000) ;
            
            while (true)
            {
            	int lineCounter = lineBatchCount ;
            	
            	do
            	{
            		line = lineReader.readLine() ;
            	
            		if (line != null)
            		{
            			// filter and send line off to server
            			
            			msgToSend = loggerName + ", LOG, " + line + "\n" ;
            			
            			System.out.print(msgToSend) ; 			
            			
                		msgSent = tcpClient.send(msgToSend) ;
                		if ( ! msgSent)
                			return ;
                		
            			// no need to send a heart beat for a while
            			
            			nextHeartbeat = System.currentTimeMillis() + (heartbeatPeriod * 1000) ;
            		}
            	}
            	while ((line != null) && (--lineCounter > 0)) ;
            	
            	//System.out.println("Killing time - process a response?");
            	
            	// time to send a heartbeat?
            	
            	long now = System.currentTimeMillis() ;
            	
            	if (now > nextHeartbeat)
            	{
            		System.out.println(loggerName + " : Sending heartbeat") ;
            		
            		msgSent = tcpClient.send(loggerName + ", HEARTBEAT\n") ;
            		if ( ! msgSent)
            			return ;
            		
            		nextHeartbeat = System.currentTimeMillis() + (heartbeatPeriod * 1000) ;
            	}
            	
            	// poll for a response
            	
/*            	line = tcpClient.readLine () ;
            	if (line != null)
            	{
            		System.out.println(loggerName + " : Received response from server : " + line);
            	}*/
            }          
		}
		catch (Exception ex)
		{
		}
	}
}

