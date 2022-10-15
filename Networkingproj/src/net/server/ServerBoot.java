package net.server;

public class ServerBoot {

	
	public ServerBoot(int port) {
		
		new Server(port);
	}

	public static void main(String[] args) {
		
		if(args.length!=1){
			System.err.println("SereverBoot >> Invalid Parameters. Please enter a PORT");
			return;
		}
		
		int port=Integer.parseInt(args[0]);
		new ServerBoot(port);
		

	}

}
