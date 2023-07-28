package adb.gambler.adbapplication.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.NotificationUtils;
import com.blankj.utilcode.util.Utils;

import adb.gambler.adbapplication.R;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/11 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class KeepAliveService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationUtils.ChannelConfig channelConfig = new NotificationUtils.ChannelConfig("remote_service", "远程控制服务", NotificationUtils.IMPORTANCE_DEFAULT);
        Notification notification = NotificationUtils.getNotification(channelConfig, new Utils.Consumer<NotificationCompat.Builder>() {
            @Override
            public void accept(NotificationCompat.Builder builder) {

            }
        });
        startForeground(11, notification);

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.view_floating_widget, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.START | Gravity.TOP;
        params.width = 1;
        params.height = 1;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }
}
