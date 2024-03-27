package adb.gambler.video.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ThreadUtils;

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

    public static final int REQUEST_CODE = 200;

    private Context context;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaCodec mediaCodec;
    private Surface surface;

    private int width, height, density;

    public ScreenRecorder(Context context){
        this.context = context;
        width = ScreenUtils.getScreenWidth();
        height = ScreenUtils.getScreenHeight();
        density = ScreenUtils.getScreenDensityDpi();

        mediaProjectionManager = (MediaProjectionManager) context.getApplicationContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void init(){
        MediaCodecInfo codecInfo = selectCodec();
        if (codecInfo == null){
            return;
        }

        try {
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 800000);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

            mediaCodec = MediaCodec.createByCodecName(codecInfo.getName());
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            surface = mediaCodec.createInputSurface();
            mediaCodec.setCallback(new MediaCodec.Callback() {
                @Override
                public void onInputBufferAvailable(@NonNull MediaCodec mediaCodec, int i) {

                }

                @Override
                public void onOutputBufferAvailable(@NonNull MediaCodec mediaCodec, int i, @NonNull MediaCodec.BufferInfo bufferInfo) {
                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i);
                    if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0){
                        bufferInfo.size = 0;
                    }
                    boolean eos = (bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
                    if (bufferInfo.size == 0 && !eos){
                        return;
                    }

                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    RemoteManager.send2Client(outputBuffer);

                    mediaCodec.releaseOutputBuffer(i, false);
                }

                @Override
                public void onError(@NonNull MediaCodec mediaCodec, @NonNull MediaCodec.CodecException e) {

                }

                @Override
                public void onOutputFormatChanged(@NonNull MediaCodec mediaCodec, @NonNull MediaFormat mediaFormat) {

                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

        mediaCodec.start();
        mediaProjection.createVirtualDisplay("ScreenCapture", width, height, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, surface, null, null);
    }

    public void start(){
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        ((Activity)context).startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void requestForResult(int code, Intent intent) {
        mediaProjection = mediaProjectionManager.getMediaProjection(code, intent);
        ThreadUtils.getCachedPool().execute(() -> init());
    }

    private MediaCodecInfo selectCodec(){
        int codeNum = MediaCodecList.getCodecCount();
        for (int i=0; i<codeNum; i++){
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()){
                continue;
            }

            String[] types = codecInfo.getSupportedTypes();
            for (String type : types){
                if (MediaFormat.MIMETYPE_VIDEO_AVC.equalsIgnoreCase(type)){
                    return codecInfo;
                }
            }
        }

        return null;
    }
}
