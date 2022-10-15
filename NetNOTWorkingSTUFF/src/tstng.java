import java.net.URL;

public class tstng {

	public static void main(String arg[]){
		try{
			URL url=new URL("http://www.javatpoint.com/java-tutorial");
			System.out.println("Protocol: "+url.getProtocol());
			System.out.println("PATH: "+url.getPath());
			System.out.println("Port: "+url.getPort());
			System.out.println("file: "+url.getFile());
			System.out.println("host: "+url.getHost());
			System.out.println("authority: "+url.getAuthority());
			System.out.println("DefltPort: "+url.getDefaultPort());
			System.out.println("Query: "+url.getQuery());
			System.out.println("Ref: "+url.getRef());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("cheese\n\n\n");
		}
		try{
			URL url=new URL("https://www.google.com/search?q=javatpoint&oq=javatpoint&sourceid=chrome&ie=UTF-8");
			System.out.println("Protocol: "+url.getProtocol());
			System.out.println("PATH: "+url.getPath());
			System.out.println("Port: "+url.getPort());
			System.out.println("file: "+url.getFile());
			System.out.println("host: "+url.getHost());
			System.out.println("authority: "+url.getAuthority());
			System.out.println("DefltPort: "+url.getDefaultPort());
			System.out.println("Query: "+url.getQuery());
			System.out.println("Ref: "+url.getRef());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("cheese");
		}
	}
}
