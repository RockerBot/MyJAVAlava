import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class umServer {
	
	Random rng=new Random();
	static int num=3333;
	//num=rng.nextInt(1000)+1024;
	public static void main(String args[]){
		try{
			ServerSocket ss=new ServerSocket(num);
			Socket s=ss.accept();
			DataInputStream dis=new DataInputStream(s.getInputStream());
			for(String str=(String)dis.readUTF();str.length()>0;str=(String)dis.readUTF())
				System.out.println("msg:  "+str);
			ss.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("server class over");
	}
}
