package adb.gambler.video.play;

import static android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2023/7/18 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class RemotePlayer extends PlayerView {

	private ExoPlayer player;
	private volatile boolean isInit = false;

	public RemotePlayer(Context context) {
		super(context);

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);

		player = new SimpleExoPlayer.Builder(context)
				.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT)
				.build();
	}

	public void play(MediaSource source){
		if (!isInit){
			isInit = true;
//			ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource();

			player.setMediaSource(source);
			player.prepare();

			setPlayer(player);
			player.play();
		}else {

		}
	}
}
