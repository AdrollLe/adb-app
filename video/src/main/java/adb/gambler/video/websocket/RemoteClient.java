package adb.gambler.video.websocket;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.MimeTypes;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
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

    private Handler handler;
    private PlayerListener listener;
    private MediaSource mediaSource;

    public RemoteClient(URI serverUri, PlayerListener listener) {
        super(serverUri);

        this.listener = listener;
        HandlerThread handlerThread = new HandlerThread("ServerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 1:
                        RemoteClient.this.listener.start();
                        break;
                    case 2:
                        reconnect();
                        break;
                    default:
                        ByteBuffer byteBuffer = (ByteBuffer) msg.obj;
                        DataSource.Factory factory = () -> new DataSource() {
                            @Override
                            public void addTransferListener(TransferListener transferListener) {

                            }

                            @Override
                            public long open(DataSpec dataSpec) throws IOException {
                                return dataSpec.length;
                            }

                            @Nullable
                            @Override
                            public Uri getUri() {
                                return null;
                            }

                            @Override
                            public void close() throws IOException {

                            }

                            @Override
                            public int read(byte[] buffer, int offset, int length) throws IOException {
                                byteBuffer.get(buffer, offset, length);
                                return length;
                            }
                        };

                        MediaItem mediaItem = new MediaItem.Builder()
                                .setUri(Uri.parse("dummy://buffer"))
                                .setMimeType(MimeTypes.BASE_TYPE_VIDEO)
                                .build();
                        MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem);
                        listener.play(mediaSource);
                        break;
                }
            }
        };
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        failCount = 0;
        send("on ready");
    }

    @Override
    public void onMessage(String message) {
        if ("start".equals(message)){
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);

        Message message = Message.obtain();
        message.what = 3;
        message.obj = bytes;
        handler.sendMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (++failCount < 3){
            try {
                Thread.sleep(3000);
                handler.sendEmptyMessage(2);
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
                handler.sendEmptyMessage(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy(){

    }
}
