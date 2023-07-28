package adb.gambler.video.record;

import android.content.Intent;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/14 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public interface ForResultCallBack {

    void requestForResult(int code, Intent intent);
}
