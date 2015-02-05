package nagaseiori.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class NioTcpServer extends Thread {

	private static final Logger log = Logger.getLogger(NioTcpServer.class.getName());  
    private InetSocketAddress inetSocketAddress;  
    private Handler handler = new ServerHandler();  
      
    public NioTcpServer(String hostname, int port) {  
        inetSocketAddress = new InetSocketAddress(hostname, port);  
    }  
      
	@Override
	public void run() {
		try {
			Selector selector = Selector.open(); // 打开选择器
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // 打开通道
			serverSocketChannel.configureBlocking(false); // 非阻塞
			serverSocketChannel.socket().bind(inetSocketAddress);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 向通道注册选择器和对应事件标识
			log.info("Server: socket server started.");
			while (true) { // 轮询
				int nKeys = selector.select();
				if (nKeys > 0) {
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = selectedKeys.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						if (key.isAcceptable()) {
							log.info("Server: SelectionKey is acceptable.");
							handler.handleAccept(key);
						} else if (key.isReadable()) {
							log.info("Server: SelectionKey is readable.");
							handler.handleRead(key);
						} else if (key.isWritable()) {
							log.info("Server: SelectionKey is writable.");
							handler.handleWrite(key);
						}
						it.remove();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
