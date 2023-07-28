package adb.gambler.video.websocket;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/19 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class RemoteServer extends ServerSocket {

	private ADBCommandListener listener;
	private Socket linkedSocket;

	public RemoteServer(int port, ADBCommandListener listener) throws IOException {
		super(port);
		this.listener = listener;
	}

	@Override
	public Socket accept() throws IOException {
		ToastUtils.showLong("已连接新client");
		if (linkedSocket == null){
			linkedSocket = super.accept();
			LocalThread localThread = new LocalThread(this);
			localThread.start();
		}

		return linkedSocket;
	}

	public void broadcast(byte[] data){
		if (linkedSocket == null || !linkedSocket.isConnected()){
			return;
		}

		OutputStream outputStream = null;
		try {
			outputStream = linkedSocket.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (outputStream != null){
					outputStream.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private static class LocalThread extends Thread {

		private WeakReference<RemoteServer> weakReference;

		public LocalThread(RemoteServer context){
			weakReference = new WeakReference<>(context);
		}

		@Override
		public void run() {
			super.run();

			while (weakReference.get().linkedSocket.isConnected() && weakReference.get().isClosed()){
				InputStream inputStream = null;
				OutputStream outputStream = null;

				try {
					inputStream = weakReference.get().linkedSocket.getInputStream();
					outputStream = weakReference.get().linkedSocket.getOutputStream();

					List<String> isList = ConvertUtils.inputStream2Lines(inputStream);
					for (String s : isList){
						if ("on ready".equals(s)){
							outputStream.write("start".getBytes());
							outputStream.flush();
						}else if (s.contains("shell:input")){
							weakReference.get().listener.sendCommand(s);
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}finally {
					try {
						if (inputStream != null){
							inputStream.close();
						}
						if (outputStream != null){
							outputStream.close();
						}
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			}

			if (weakReference.get().linkedSocket != null){
				weakReference.get().linkedSocket = null;
			}
		}
	}
}
