package adb.gambler.adbapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import adb.gambler.adbapplication.service.KeepAliveService;
import adb.gambler.adbapplication.view.RVAdapter;
import adb.gambler.jadb.lib.AdbConnection;
import adb.gambler.jadb.lib.AdbCrypto;

public class ClientActivity extends AppCompatActivity {

    private TextView textView;
    private RecyclerView recyclerView;

    private AdbConnection adbConnection;

    private boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        textView = findViewById(R.id.tv_info);
        recyclerView = findViewById(R.id.rv);

        PermissionUtils.permission(Manifest.permission.ACCESS_WIFI_STATE).callback(new PermissionUtils.SingleCallback() {
            @Override
            public void callback(boolean isAllGranted, @NonNull List<String> granted, @NonNull List<String> deniedForever, @NonNull List<String> denied) {
                if (isAllGranted){
                    String wifi = NetworkUtils.getIpAddressByWifi();
                    String ip = NetworkUtils.getIPAddress(true);
                    textView.setText("wifi = " + wifi + "\nip = " + ip + (result ? " 本地启动成功" : " 本地启动失败"));
                }
            }
        }).request();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RVAdapter adapter = new RVAdapter();
        recyclerView.setAdapter(adapter);

        try {
            AdbCrypto crypto = AdbCrypto.generateAdbKeyPair(data -> Base64.encodeToString(data, Base64.NO_WRAP));

            adbConnection = AdbConnection.create(new Socket("127.0.0.1", 5555), crypto);
            result = adbConnection.connect();

            if (!result){
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(new Intent(this, KeepAliveService.class));
            }else {
                startService(new Intent(this, KeepAliveService.class));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            adbConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}