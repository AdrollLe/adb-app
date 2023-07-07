package adb.gambler.adbapplication;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ThreadUtils;

import java.io.IOException;
import java.net.Socket;

import adb.gambler.jadb.lib.AdbConnection;
import adb.gambler.jadb.lib.AdbCrypto;

public class ServerActivity extends AppCompatActivity {

	private AdbConnection adbConnection;

	private EditText editText;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);

		editText = findViewById(R.id.server_et);
		textView = findViewById(R.id.server_tv_text);
		findViewById(R.id.server_tv_connect).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ThreadUtils.getSinglePool().execute(() -> {
					try {
						if (adbConnection == null){
							AdbCrypto crypto = AdbCrypto.generateAdbKeyPair(data -> Base64.encodeToString(data, Base64.NO_WRAP));

							adbConnection = AdbConnection.create(new Socket(editText.getText().toString(), 5555), crypto);
							boolean result = adbConnection.connect();
							if (result){
								textView.setText("连接成功，将在3秒后镜像");
							}

							adbConnection.open("shell:input keyevent 24");
						}else {

						}
					} catch (Exception e) {
						e.printStackTrace();
						if (adbConnection != null){
							try {
								adbConnection.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							adbConnection = null;
						}
					}
				});
			}});
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