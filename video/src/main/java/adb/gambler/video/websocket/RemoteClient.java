package adb.gambler.video.websocket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.ByteBuffer;

import adb.gambler.video.play.ByteArrayDataSource;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/19 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class RemoteClient extends WebSocketClient {

    private int failCount = 0;

    private LocalHandler handler;
    private PlayerListener listener;

    public RemoteClient(URI serverUri, PlayerListener listener) {
        super(serverUri);

        this.listener = listener;

        ThreadUtils.getCachedPool().execute(() -> {
            Looper.prepare();
            handler = new LocalHandler(RemoteClient.this);
            Looper.loop();
        });
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        failCount = 0;
        send("on ready");
    }

    @Override
    public void onMessage(String message) {
        if ("start".equals(message)){
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);

        Message message = Message.obtain();
        message.what = 0;
        message.obj = bytes;
        handler.sendMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (++failCount < 3){
            try {
                Thread.sleep(3000);
                handler.sendEmptyMessage(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Exception ex) {
        if (++failCount < 3){
            try {
                Thread.sleep(3000);
                handler.sendEmptyMessage(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class LocalHandler extends Handler {

        private WeakReference<RemoteClient> weakReference;

        public LocalHandler(RemoteClient client){
            weakReference = new WeakReference<>(client);
            sendEmptyMessage(1);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    weakReference.get().connect();
                    break;
                case 2:
                    weakReference.get().listener.start();
                    break;
                case 3:
                    weakReference.get().reconnect();
                    break;
                default:
                    ByteBuffer bytes = (ByteBuffer) msg.obj;
                    byte[] data = bytes.array();
                    DataSource.Factory factory = new DataSource.Factory() {
                        @Override
                        public DataSource createDataSource() {
                            return new ByteArrayDataSource(data);
                        }
                    };

                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(new MediaItem.Builder().build());
                    weakReference.get().listener.play(mediaSource);
                    break;
            }
        }
    }
}
