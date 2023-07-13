package adb.gambler.adbapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ThreadUtils;

import java.io.IOException;
import java.net.Socket;

import adb.gambler.adbapplication.manager.ConstantManager;
import adb.gambler.adbapplication.service.KeepAliveService;
import adb.gambler.adbapplication.view.RVAdapter;
import adb.gambler.jadb.lib.AdbConnection;
import adb.gambler.jadb.lib.AdbCrypto;

public class ClientActivity extends AppCompatActivity {

    private TextView textView;
    private RecyclerView recyclerView;

    private AdbConnection adbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        textView = findViewById(R.id.tv_info);
        recyclerView = findViewById(R.id.rv);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RVAdapter adapter = new RVAdapter();
        recyclerView.setAdapter(adapter);

        ThreadUtils.getSinglePool().execute(() -> {
            try {
                AdbCrypto crypto = AdbCrypto.generateAdbKeyPair(data -> Base64.encodeToString(data, Base64.NO_WRAP));

                adbConnection = AdbConnection.create(new Socket("127.0.0.1", 5555), crypto);
                boolean result = adbConnection.connect();

                if (!result){
                    textView.setText("wifi = " + ConstantManager.getWifi());
                }else {
                    textView.setText("wifi = " + ConstantManager.getWifi() + "\nip = " + ConstantManager.getIp() + " 本地启动成功");
                }
            }catch (Exception e){
                textView.setText(e.getMessage());
                e.printStackTrace();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        startForegroundService(new Intent(ClientActivity.this, KeepAliveService.class));
                    }else {
                        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        startService(new Intent(ClientActivity.this, KeepAliveService.class));
                    }
                }

                @Override
                public void onDenied() {

                }
            });
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