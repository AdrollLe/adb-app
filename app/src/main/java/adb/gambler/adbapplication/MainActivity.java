package adb.gambler.adbapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.util.List;

import adb.gambler.adbapplication.manager.ConstantManager;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.main_tv_controller).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, ServerActivity.class));
			}
		});

		findViewById(R.id.main_tv_control).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, ClientActivity.class));
			}
		});

		PermissionUtils.permission(Manifest.permission.ACCESS_WIFI_STATE).callback(new PermissionUtils.SingleCallback() {
			@Override
			public void callback(boolean isAllGranted, @NonNull List<String> granted, @NonNull List<String> deniedForever, @NonNull List<String> denied) {
				if (isAllGranted){
					String wifi = NetworkUtils.getIpAddressByWifi();
					String ip = NetworkUtils.getIPAddress(true);

					ConstantManager.setWifi(wifi);
					ConstantManager.setIp(ip);
				}
			}
		}).request();
	}
}