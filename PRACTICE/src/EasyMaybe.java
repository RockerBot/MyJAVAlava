import java.util.Iterator;
import java.util.LinkedList;

public class EasyMaybe {
	static LinkedList<Integer> list=new LinkedList<>();
	Integer value;
	EasyMaybe next;
	EasyMaybe(int value){
		this.value=value;
		this.next=null;
	}
	public static void main(String args[]){
		EasyMaybe obj=new EasyMaybe(1);
		//obj.reverseO(obj);
		for(int i=0;i<10;i++)list.add(i);
		for(int i:list)System.out.println(i+" "+list.get(i));
		System.out.println(list);
		//Iterator iter=list.iterator();
	}
	
	public EasyMaybe reverseO(EasyMaybe head){
		//a->b->c->d->null
		//d->c->b->a->null
		EasyMaybe previous=null;
		while(head.next!=null){
			EasyMaybe temp=head.next;
			head.next=previous;
			previous=head;
			head=temp;
		}
		return head;
	}
}
