package net.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

	private DatagramSocket socket;
	private int port;
	private boolean running=false;
	
	public Server(int port) {
		try{
			
			this.port=port;
			
			socket=new DatagramSocket(port);
			running=true;
			receive();
			
			System.out.println("Serever>> Started on PORT "+port);
			
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void receive(){
		
		Thread thread=new Thread("Waiter"){
			public void run(){
				try{
				
					while(running){
						
						byte[] rdata=new byte[1024];
						DatagramPacket packet=new DatagramPacket(rdata, rdata.length);
						socket.receive(packet);
						
						String msg=new String(rdata);
						msg=msg.substring(0,msg.indexOf("/e/"));
						
						System.out.println(packet.getAddress().getHostAddress()+":"+packet.getPort()+" >> "+msg);
					}
				} catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}; thread.start();
		
	}

}
