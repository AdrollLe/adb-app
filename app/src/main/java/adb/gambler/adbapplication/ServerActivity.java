package adb.gambler.adbapplication;

import android.os.Bundle;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;

import adb.gambler.adbapplication.manager.CommandManager;
import adb.gambler.adbapplication.manager.ConstantManager;
import adb.gambler.adbapplication.view.ControlView;
import adb.gambler.jadb.lib.AdbConnection;
import adb.gambler.jadb.lib.AdbCrypto;
import adb.gambler.video.play.RemotePlayer;
import adb.gambler.video.websocket.PlayerListener;
import adb.gambler.video.websocket.RemoteManager;

public class ServerActivity extends AppCompatActivity {

	private AdbConnection adbConnection;

	private EditText etIp, etPort;
	private TextView tvContent, tvConnect;

	private CountDownThread countDownThread;
	private ControlView controlView;
	private RemotePlayer remotePlayer;

	private boolean isWeb = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		etIp = findViewById(R.id.server_et_ip);
		etPort = findViewById(R.id.server_et_port);
		tvContent = findViewById(R.id.server_tv_text);
		tvConnect = findViewById(R.id.server_tv_connect);

		tvConnect.setOnClickListener(view -> ThreadUtils.getSinglePool().execute(() -> {
			if (!StringUtils.isEmpty(etPort.getText().toString())){
				isWeb = true;
				RemoteManager.initClient(etIp.getText().toString(), etPort.getText().toString(), new PlayerListener() {
					@Override
					public void start() {
						remotePlayer = new RemotePlayer(ServerActivity.this);
						countDownThread.start();
					}

					@Override
					public void play(com.google.android.exoplayer2.source.MediaSource source) {
						remotePlayer.play(source);
					}
				});
				RemoteManager.send2Server("on ready".getBytes());
			}else {
				connectToLan(etIp.getText().toString());
			}
		}));
	}

	@Override
	protected void onStart() {
		super.onStart();

		countDownThread = new CountDownThread(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		destroyConnection();
	}

	/**
	 * 局域网连接
	 * @param connectIP 局域网ip
	 */
	private void connectToLan(String connectIP){
		try {
			if (adbConnection == null){
				KeyboardUtils.hideSoftInput(ServerActivity.this);
				// adb连接client
				AdbCrypto crypto = AdbCrypto.generateAdbKeyPair(data -> Base64.encodeToString(data, Base64.NO_WRAP));

				adbConnection = AdbConnection.create(new Socket(connectIP, 5555), crypto);
				boolean result = adbConnection.connect();
				if (result){
					tvConnect.setText("断开连接");
					tvContent.setText("连接成功，将在3秒后镜像");

					countDownThread.start();
				}

				controlView = new ControlView(this, adbConnection);
			}else {
				tvConnect.setText("连接");
				countDownThread.interrupt();
				destroyConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
			destroyConnection();
		}
	}

	private void destroyConnection(){
		if (adbConnection != null){
			try {
				adbConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class CountDownThread extends Thread{

		private WeakReference<ServerActivity> weakReference;

		private int count = 3;

		public CountDownThread(ServerActivity activity){
			weakReference = new WeakReference<>(activity);
		}

		@Override
		public void run() {
			super.run();

			while (count > 0){
				try {
					Thread.sleep(1000);
					count--;
					weakReference.get().tvContent.setText("连接成功，将在" + count + "秒后镜像");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			ThreadUtils.getMainHandler().post(() -> {
				try {
					// 将client的app推到后台
					if (!weakReference.get().isWeb){
						weakReference.get().adbConnection.open(CommandManager.COMMAND_HOME);
					}
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

				if (weakReference.get().isWeb){
					((ViewGroup)weakReference.get().findViewById(android.R.id.content)).addView(weakReference.get().controlView.getView());
				}else {
					((ViewGroup)weakReference.get().findViewById(android.R.id.content)).addView(weakReference.get().remotePlayer);
				}
			});
		}
	}
}