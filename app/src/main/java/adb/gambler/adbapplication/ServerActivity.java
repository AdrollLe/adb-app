package adb.gambler.adbapplication;

import android.Manifest;
import android.os.Bundle;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.List;

import adb.gambler.adbapplication.manager.CommandManager;
import adb.gambler.adbapplication.view.ControlView;
import adb.gambler.jadb.lib.AdbConnection;
import adb.gambler.jadb.lib.AdbCrypto;

public class ServerActivity extends AppCompatActivity {

	private AdbConnection adbConnection;

	private EditText editText;
	private TextView tvContent, tvConnect;

	private CountDownThread countDownThread;
	private ControlView controlView;

	private String wifi, ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		editText = findViewById(R.id.server_et);
		tvContent = findViewById(R.id.server_tv_text);
		tvConnect = findViewById(R.id.server_tv_connect);

		PermissionUtils.permission(Manifest.permission.ACCESS_WIFI_STATE).callback(new PermissionUtils.SingleCallback() {
			@Override
			public void callback(boolean isAllGranted, @NonNull List<String> granted, @NonNull List<String> deniedForever, @NonNull List<String> denied) {
				if (isAllGranted){
					wifi = NetworkUtils.getIpAddressByWifi();
					ip = NetworkUtils.getIPAddress(true);
				}
			}
		}).request();

		tvConnect.setOnClickListener(view -> ThreadUtils.getSinglePool().execute(() -> {
			String connectIP = editText.getText().toString();
			if (StringUtils.isEmpty(wifi)){

				return;
			}

			String[] t1 = wifi.split(".");
			String[] t2 = connectIP.split(".");
			if (t1[0].equals(t2[0]) && t1[1].equals(t2[1])){
				connectToLAN(connectIP);
			}else {

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

	private void connectToLAN(String connectIP){
		try {
			if (adbConnection == null){
				KeyboardUtils.hideSoftInput(ServerActivity.this);
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
					weakReference.get().adbConnection.open(CommandManager.COMMAND_HOME);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				((ViewGroup)weakReference.get().findViewById(android.R.id.content)).addView(weakReference.get().controlView.getView());
			});
		}
	}
}