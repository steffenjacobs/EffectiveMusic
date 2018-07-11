package me.steffenjacobs.effectivemusic;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javazoom.jlgui.basicplayer.BasicPlayerException;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
@DependsOn("audioPlayer")
public class AudioEffectManager {

	private static final long TIMER_INTERVAL_MILLIS = 100;

	@Autowired
	AudioPlayer audioPlayer;

	private boolean fading = false;

	public void fadeTo(final double targetGain, long fadelengthInMilis) {
		if (fading) {
			throw new RuntimeException("Already fading.");
		}
		fading = true;

		final double initialGain = audioPlayer.getGain();
		if (targetGain == initialGain) {
			return;
		}
		final long steps = fadelengthInMilis / TIMER_INTERVAL_MILLIS;
		final AudioPlayer player = audioPlayer;
		boolean fadeUp = targetGain - initialGain > 0;
		final Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			double currentGain = initialGain;

			@Override
			public void run() {
				currentGain += (targetGain - initialGain) / steps;
				try {
					player.setGain(currentGain < 0 ? 0 : currentGain > 1 ? 1 : currentGain);

					if ((fadeUp && currentGain >= targetGain) || (!fadeUp && currentGain <= targetGain)) {
						t.cancel();
						player.setGain(targetGain);
						fading = false;
					}
				} catch (BasicPlayerException e) {
					e.printStackTrace();
				}

			}
		}, 0, TIMER_INTERVAL_MILLIS);
	}

	public void fadeOut(int length) {
		fadeTo(0, length);
	}
}
