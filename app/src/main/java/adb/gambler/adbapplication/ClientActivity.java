package adb.gambler.adbapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import adb.gambler.video.record.ScreenRecorder;
import adb.gambler.video.websocket.ADBCommandListener;
import adb.gambler.video.websocket.RemoteManager;

public class ClientActivity extends AppCompatActivity {

    private TextView textView;
    private RecyclerView recyclerView;

    private AdbConnection adbConnection;
    private ScreenRecorder recorder;

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
            int port = RemoteManager.initServer(new ADBCommandListener() {
                @Override
                public void sendCommand(String command) {
                    try {
                        adbConnection.open(command);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void start() {
                    try {
                        // 连接到本地adb
                        AdbCrypto crypto = AdbCrypto.generateAdbKeyPair(data -> Base64.encodeToString(data, Base64.NO_WRAP));

                        adbConnection = AdbConnection.create(new Socket("127.0.0.1", 5555), crypto);
                        boolean result = adbConnection.connect();

                        if (result){
                            // 开启录屏
                            recorder = new ScreenRecorder(ClientActivity.this);
                            recorder.start();
                        }
                    }catch (Exception e){
                        textView.setText(e.getMessage());
                    }
                }
            }, ConstantManager.getIp());

            textView.setText("wifi = " + ConstantManager.getWifi() + "\nip = " + ConstantManager.getIp() + "\nport = " + port);
        });

        // 开启保活服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        startForegroundService(new Intent(ClientActivity.this, KeepAliveService.class));
                    }else {
                        // FIXME: 2023/7/19 用其他方法保活
//                        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                        startService(new Intent(ClientActivity.this, KeepAliveService.class));
                    }
                }

                @Override
                public void onDenied() {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        recorder.requestForResult(requestCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (adbConnection != null){
                adbConnection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}