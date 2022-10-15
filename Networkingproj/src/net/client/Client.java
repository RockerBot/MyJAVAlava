package net.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

	private static DatagramSocket socket;
	
	public static void start(){
		try{
			socket=new DatagramSocket();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void send(String msg, String ip, int port){
		try{
			
			msg+="/e/";
			byte[] data=msg.getBytes();
			DatagramPacket packet=new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
			
			socket.send(packet);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
