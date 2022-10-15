import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
public class Main {
	public static void main(String[] args) throws IOException {
		User user = new User();
		user.name = "Bro";
		user.password = "I<3Pizza";
		FileOutputStream fileOut=new FileOutputStream("UserInfo.ser");
		ObjectOutputStream out =new ObjectOutputStream(fileOut);
		out.writeObject(user);
		out.close();
		fileOut.close();
		System.out.println("object Info saved :)");
	}
}