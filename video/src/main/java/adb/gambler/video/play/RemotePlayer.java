package adb.gambler.video.play;

import static com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT;

import android.content.Context;

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

	private SimpleExoPlayer player;

	public RemotePlayer(Context context) {
		super(context);

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);

		player = new SimpleExoPlayer.Builder(context)
				.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT)
				.build();

		setPlayer(player);
	}

	public void play(MediaSource source){
		player.setMediaSource(source);
		player.prepare();
		player.play();
	}
}
