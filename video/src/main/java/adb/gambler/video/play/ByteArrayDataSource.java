package adb.gambler.video.play;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/19 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class ByteArrayDataSource implements DataSource {

    private final byte[] data;
    private int offset;
    private int length;

    public ByteArrayDataSource(byte[] data) {
        this.data = data;
        this.offset = 0;
        this.length = data.length;
    }

    @Override
    public void addTransferListener(TransferListener transferListener) {

    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        offset = (int) dataSpec.position;
        length = (int) Math.min(dataSpec.length, data.length - offset);
        return length;
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
    public int read(byte[] target, int offset, int length) throws IOException {
        int remaining = this.length - offset;
        if (remaining <= 0) {
         return -1;
        }
        int bytesRead = Math.min(length, remaining);
        System.arraycopy(data, this.offset + offset, target, 0, bytesRead);
        return bytesRead;
    }
}
