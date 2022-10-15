import java.io.Serializable;
public class User implements Serializable{
	String name;
	transient String password;
	public void sayhello(){
		System.out.println("Hello "+name);
	}
}