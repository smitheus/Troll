package za.co.sb.Troll.tcp;

public class LogTrollServer
{
	public static void main(String[] args)
	{
		System.out.println("Starting log troll server");

		// TODO need a jolly old config file
		
		TcpUtils tcpServer = new TcpUtils (6789) ;
		
		tcpServer.startUp() ;
		
		tcpServer.run();
		
		System.out.println("Terminating the server");
	}
}
