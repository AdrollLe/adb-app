package adb.gambler.video.websocket;

import com.google.android.exoplayer2.source.MediaSource;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/19 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public interface PlayerListener {

    void start();

    void play(MediaSource source);
}
