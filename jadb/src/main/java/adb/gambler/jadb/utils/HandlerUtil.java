package adb.gambler.jadb.utils;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * <p> File description: <p>
 * <p> Creator: Adroll   <p>
 * <p> Created date: 2022/3/20 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class HandlerUtil extends Handler{

    private static HandlerUtil instance;

    private TextView textView;

    public static HandlerUtil getInstance(){
        if (instance == null){
            instance = new HandlerUtil();
        }

        return instance;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        String text = textView.getText().toString();
//        if (text.contains("\n")){
//            String[] arrays = text.split("\n");
//            if (arrays.length == 4){
//                text = arrays[1] + "\n" + arrays[2] + "\n" + arrays[3] + "\n" + msg.obj.toString();
//            }else {
//                text = text + "\n" + msg.obj.toString();
//            }
//        } else if (text == null || text.length() == 0){
//            text = msg.obj.toString();
//        } else {
            text = text + "\n" + msg.obj.toString();
//        }

        textView.setText(text);
    }
}
