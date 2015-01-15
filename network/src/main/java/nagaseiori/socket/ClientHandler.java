package nagaseiori.socket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter{

	 public void messageReceived(IoSession session, Object message) throws Exception {
	        String content = message.toString();
	        System.out.println("client receive a message is : " + content);
	    }

	    public void messageSent(IoSession session, Object message) throws Exception {
	        System.out.println("messageSent -> ：" + message);
	    }
}
