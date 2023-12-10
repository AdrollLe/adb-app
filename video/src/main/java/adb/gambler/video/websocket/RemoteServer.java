package adb.gambler.video.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/19 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class RemoteServer extends WebSocketServer {

	private ADBCommandListener listener;
	private WebSocket client;

	public RemoteServer(int port, ADBCommandListener listener) {
		super(new InetSocketAddress(port));
		this.listener = listener;
		start();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		if (client == null){
			client = conn;
			listener.start();
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if ("on ready".equals(message)){
			broadcast("start");
		}else if (message.contains("shell:input")){
			listener.sendCommand(message);
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {

	}

	@Override
	public void onStart() {

	}
}
