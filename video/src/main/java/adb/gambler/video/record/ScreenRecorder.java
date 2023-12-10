package adb.gambler.video.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.view.Surface;

import com.blankj.utilcode.util.ScreenUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import adb.gambler.video.websocket.RemoteManager;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/14 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class ScreenRecorder implements ForResultCallBack{

    private static final int REQUEST_CODE = 200;

    private Context context;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaCodec mediaCodec;
    private Surface surface;

    private int width, height, density;

    public ScreenRecorder(Context context){
        this.context = context;

        mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        ((Activity)context).startActivityForResult(intent, 1001);
    }

    private void init(){
        width = ScreenUtils.getScreenWidth();
        height = ScreenUtils.getScreenHeight();
        density = ScreenUtils.getScreenDensityDpi();

        try {
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 2 * 1024 * 1024);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            surface = mediaCodec.createInputSurface();
        }catch (IOException e){
            e.printStackTrace();
        }

        mediaCodec.start();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        mediaProjection.createVirtualDisplay("ScreenCapture", width, height, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, surface, null, null);
        while (true){
            int index = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
            if (index >= 0){
                ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(index);
                byte[] data = new byte[bufferInfo.size];
                outputBuffer.get(data);
                outputBuffer.clear();
                RemoteManager.send2Client(data);

                mediaCodec.releaseOutputBuffer(index, false);
            }
        }
    }

    public void start(){
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        ((Activity)context).startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void requestForResult(int code, Intent intent) {
        mediaProjection = mediaProjectionManager.getMediaProjection(REQUEST_CODE, intent);
        init();
    }
}
