
public class Threadilizing implements Runnable {
	
	private int k=0;
	@Override
	public void run() {
		System.out.println("thread is runnig"+k++ + Thread.currentThread());
		System.out.println(Thread.currentThread().getName());
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
			System.out.println(e);
		}
		System.out.println("thread is $ $ runnig"+k--+Thread.currentThread());
		System.out.println(Thread.currentThread());
	}
	public static void main(String args[]){  
		Threadilizing m1=new Threadilizing();
		Threadilizing m2=new Threadilizing();
		Thread t1 =new Thread(m1);
		Thread t2 =new Thread(m2);
		System.out.println("Name of t1:"+t1.getName());
		System.out.println("Name of t2:"+t2.getName());
		System.out.println("id of t1:"+t1.getId());
		t1.start();
		t2.start();
		

		System.out.println(Thread.currentThread());
	 }
		
}
