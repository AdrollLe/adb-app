package adb.gambler.video.websocket;

import com.blankj.utilcode.util.ConvertUtils;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import adb.gambler.video.play.ByteArrayDataSource;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/19 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class RemoteClient {

    private int failCount = 0;

    private Socket socket;
    private PlayerListener listener;

    private String url;
    private int port;

    public RemoteClient(String url, int port, PlayerListener listener) {
        this.url = url;
        this.port = port;
        this.listener = listener;

        connect();
    }

    public void send(byte[] data){
        if (socket == null || !socket.isConnected()){
            return;
        }

        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null){
                    outputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void connect(){
        try {
            socket = new Socket(url, port);
            LocalThread localThread = new LocalThread(this);
            localThread.start();
            listener.start();
        } catch (IOException e) {
            onFail();
        }
    }

    private static class LocalThread extends Thread{

        private WeakReference<RemoteClient> weakReference;

        public LocalThread(RemoteClient client){
            weakReference = new WeakReference<>(client);
        }

        @Override
        public void run() {
            super.run();

            try {
                OutputStream outputStream = weakReference.get().socket.getOutputStream();
                outputStream.write("on ready".getBytes());

            }catch (IOException e){
                e.printStackTrace();
            }

            boolean init = false;
            while (weakReference.get().socket.isConnected() && weakReference.get().socket.isClosed()){
                InputStream inputStream = null;
                try {
                    inputStream = weakReference.get().socket.getInputStream();

                    if (!init){
                        List<String> list = ConvertUtils.inputStream2Lines(inputStream);
                        for (String s : list){
                            if ("start".equals(s)){
                                init = true;
                                break;
                            }
                        }

                        continue;
                    }

                    byte[] data = ConvertUtils.inputStream2Bytes(inputStream);
                    DataSource.Factory factory = new DataSource.Factory() {
                        @Override
                        public DataSource createDataSource() {
                            return new ByteArrayDataSource(data);
                        }
                    };

                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(new MediaItem.Builder().build());
                    weakReference.get().listener.play(mediaSource);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        if (inputStream != null){
                            inputStream.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void onFail() {
        if (++failCount < 3){
            try {
                Thread.sleep(3000);
                connect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
