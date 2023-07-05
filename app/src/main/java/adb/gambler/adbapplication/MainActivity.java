package adb.gambler.adbapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;

import adb.gambler.jadb.lib.AdbConnection;
import adb.gambler.jadb.lib.AdbCrypto;
import adb.gambler.jadb.utils.HandlerUtil;

public class MainActivity extends Activity {

	private TextView textView;

	private AdbConnection adbConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = findViewById(R.id.tv_main);
		HandlerUtil.getInstance().setTextView(textView);

		new Thread(() -> {
			try {
				AdbCrypto crypto = AdbCrypto.generateAdbKeyPair(data -> Base64.encodeToString(data, Base64.NO_WRAP));

				adbConnection = AdbConnection.create(new Socket("127.0.0.1", 5555), crypto);
				adbConnection.connect();

				adbConnection.open("shell:input keyevent 24");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (adbConnection != null){
			try {
				adbConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}