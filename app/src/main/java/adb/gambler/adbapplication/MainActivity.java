package adb.gambler.adbapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import adb.gambler.adbapplication.manager.ConstantManager;
import adb.gambler.adbapplication.utils.NetworkUtils;

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
				if (StringUtils.isEmpty(ConstantManager.getWifi()) || StringUtils.isEmpty(ConstantManager.getIp())){
					ToastUtils.showLong("应用正在初始化，请稍后再试！");
					return;
				}
				startActivity(new Intent(MainActivity.this, ClientActivity.class));
			}
		});

		PermissionUtils.permission(Manifest.permission.ACCESS_WIFI_STATE).callback(new PermissionUtils.SingleCallback() {
			@Override
			public void callback(boolean isAllGranted, @NonNull List<String> granted, @NonNull List<String> deniedForever, @NonNull List<String> denied) {
				if (isAllGranted){
					ThreadUtils.getCachedPool().execute(new Runnable() {
						@Override
						public void run() {
							String wifi = com.blankj.utilcode.util.NetworkUtils.getIpAddressByWifi();
							String ip = NetworkUtils.getHostIp();

							ConstantManager.setWifi(wifi);
							ConstantManager.setIp(ip);
						}
					});
				}
			}
		}).request();
	}
}