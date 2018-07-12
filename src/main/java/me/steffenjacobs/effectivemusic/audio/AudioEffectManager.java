package me.steffenjacobs.effectivemusic.audio;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
@DependsOn("vlcPlayer")
public class AudioEffectManager implements FadeCompleteListener {

	@Autowired
	VLCMediaPlayerAdapter vlcPlayer;

	private AtomicBoolean fading = new AtomicBoolean(false);
	private Fade fade;

	public void fadeTo(final double targetGain, long fadeLengthInMillis) {
		final double initialGain = vlcPlayer.getGain();

		if (targetGain == initialGain) {
			if (!fading.get()) {
				return;
			}
		}

		Fade fadeIP;
		if (fading.get() && fade != null) {
			fadeIP = fade;
			fade.pauseFade();
		} else {
			fadeIP = new Fade(this);
		}
		fadeIP.setTargetGain(targetGain);
		fadeIP.setInitalGain(initialGain);
		fadeIP.setSteps(fadeLengthInMillis / Fade.TIMER_INTERVAL_MILLIS);
		fadeIP.setAudioPlayer(vlcPlayer);
		fadeIP.setFadeUp(targetGain - initialGain > 0);

		if (fading.get() && fade != null) {
			fade.resumeFade();
		} else {
			fade = fadeIP;
			fading.set(true);
			fade.runAsync();
		}
	}

	public void fadeOut(int length) {
		fadeTo(0, length);
	}

	@Override
	public void onFadeComplete() {
		fading.set(false);
	}
}
