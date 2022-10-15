

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

		User user=null;
		FileInputStream fileIn=new FileInputStream("C:\\Users\\inodi\\Desktop\\vikas IMPORTANT\\vikas IMPORTANT rubbish\\APPs\\ECLIPSE\\serializationLearninging\\UserInfo.ser");
		ObjectInputStream in =new ObjectInputStream(fileIn);
		user = (User) in.readObject();
		in.close();
		fileIn.close();
		
		System.out.println(user.name);
		System.out.println(user.password);
		user.sayhello();
	}

}
