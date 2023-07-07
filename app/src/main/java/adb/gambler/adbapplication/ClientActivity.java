package adb.gambler.adbapplication;

import android.Manifest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.io.IOException;
import java.util.List;

import adb.gambler.jadb.lib.AdbConnection;

public class ClientActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;

    private AdbConnection adbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        textView = findViewById(R.id.tv_info);
        editText = findViewById(R.id.et_input);
        button = findViewById(R.id.bt_run);
        recyclerView = findViewById(R.id.rv);

        PermissionUtils.permission(Manifest.permission.ACCESS_WIFI_STATE).callback(new PermissionUtils.SingleCallback() {
            @Override
            public void callback(boolean isAllGranted, @NonNull List<String> granted, @NonNull List<String> deniedForever, @NonNull List<String> denied) {
                if (isAllGranted){
                    String wifi = NetworkUtils.getIpAddressByWifi();
                    String ip = NetworkUtils.getIPAddress(true);
                    textView.setText("wifi = " + wifi + "\nip = " + ip);
                }
            }
        }).request();

        textView.setOnClickListener(view -> textView.setBackgroundColor(ClientActivity.this.getResources().getColor(R.color.design_default_color_error)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        button.setOnClickListener(view -> new Thread(() -> {
            try {
                adbConnection.open("shell:" + editText.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start());

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RVAdapter adapter = new RVAdapter();
        recyclerView.setAdapter(adapter);
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