package adb.gambler.adbapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.Socket;

import adb.gambler.jadb.lib.AdbConnection;
import adb.gambler.jadb.lib.AdbCrypto;
import adb.gambler.jadb.utils.HandlerUtil;

public class SecondActivity extends Activity {

    private TextView textView;
    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;

    private AdbConnection adbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = findViewById(R.id.tv_info);
        editText = findViewById(R.id.et_input);
        button = findViewById(R.id.bt_run);
        recyclerView = findViewById(R.id.rv);

        HandlerUtil.getInstance().setTextView(textView);
        textView.setOnClickListener(view -> textView.setTextColor(SecondActivity.this.getResources().getColor(R.color.design_default_color_error)));

        //noinspection AlibabaAvoidManuallyCreateThread
        new Thread(() -> {
            try {
                AdbCrypto crypto = AdbCrypto.generateAdbKeyPair(data -> Base64.encodeToString(data, Base64.NO_WRAP));

                adbConnection = AdbConnection.create(new Socket("10.118.49.183", 5555), crypto);
                adbConnection.connect();

                adbConnection.open("shell:input keyevent 24");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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