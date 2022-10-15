import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerSNAKE implements Runnable{
	private int port;
	private boolean running;
	private Selector slctr;
	private ServerSocket srvrSckt;
	private ByteBuffer bytBfr;	
	public ServerSNAKE(int port, int bfrSize){
		this.port=port;
		this.bytBfr=ByteBuffer.allocate(bfrSize);
	}
	public void start(){
		new Thread(this).start();
	}
	@Override
	public void run() {
		running=true;
		while(running){
			try {
				int client=slctr.select();
				if(client==0){
					continue;
				}
				
				Set<SelectionKey> keys=slctr.selectedKeys();
				Iterator<SelectionKey> it=keys.iterator();
				
				while(it.hasNext()){
					SelectionKey key=(SelectionKey)it.next();
					
					if((key.readyOps() & SelectionKey.OP_ACCEPT)==SelectionKey.OP_ACCEPT){
						Socket sckt=srvrSckt.accept();
						System.out.println("Connection from: "+sckt);
						SocketChannel scktChnl=sckt.getChannel();
						scktChnl.configureBlocking(false);
						scktChnl.register(slctr, SelectionKey.OP_READ);
					}else if((key.readyOps() & SelectionKey.OP_READ)==SelectionKey.OP_READ){
						SocketChannel channel=null;
						channel=(SocketChannel)key.channel();
						boolean connection=readData(channel,bytBfr);
						if(!connection){
							key.cancel();
							Socket sckt=null;
							sckt=channel.socket();
							sckt.close();
						}
					}
					keys.clear();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void open(){
		ServerSocketChannel srvrChnl;
		try {
			srvrChnl=ServerSocketChannel.open();
			srvrChnl.configureBlocking(false);
			srvrSckt=srvrChnl.socket();
			InetSocketAddress adrss=new InetSocketAddress(port);
			srvrSckt.bind(adrss);
			slctr=Selector.open();
			srvrChnl.register(slctr, SelectionKey.OP_ACCEPT);
			System.out.println("SERVER CREATED on port"+port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean readData(SocketChannel chnnl, ByteBuffer bffr){
		return true;
	}
}
