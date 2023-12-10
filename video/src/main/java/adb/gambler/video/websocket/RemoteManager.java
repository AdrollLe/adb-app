package adb.gambler.video.websocket;

import com.blankj.utilcode.util.SPUtils;

import java.net.URI;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/18 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class RemoteManager {

	private static RemoteServer server;
	private static RemoteClient client;

	private static final String DEBUG_WEBSOCKET_PORT = "Gambler_websocket_port";
	private static int port = 5002;

	public static int initServer(ADBCommandListener listener, String ip){
		if (SPUtils.getInstance().contains(DEBUG_WEBSOCKET_PORT)){
			port = SPUtils.getInstance().getInt(DEBUG_WEBSOCKET_PORT);
			port = port + 3;
			if (port > 10000){
				port = 5001;
			}
		}
		SPUtils.getInstance().put(DEBUG_WEBSOCKET_PORT, port);

		try {
			server = new RemoteServer(port, listener);
		}catch (Exception e){
			e.printStackTrace();
		}

		return port;
	}

	public static void initClient(String ip, PlayerListener listener){
		client = new RemoteClient(URI.create(ip), listener);
	}

	public static void send2Client(byte[] data){
		server.broadcast(data);
	}

	public static void send2Server(byte[] data){
		client.send(data);
	}
}
