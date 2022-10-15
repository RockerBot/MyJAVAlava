import java.io.DataOutputStream;
import java.net.Socket;

public class umClient {
	public static void main(String args[]){
		try{
			Socket s= new Socket("localhost", 3333);
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			dos.writeUTF("Helo SERVER");
			dos.writeUTF("sen");
			dos.writeUTF("ten");
			dos.writeUTF("ce");
			dos.flush();
			dos.close();
			s.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("client class over");
	}
}
