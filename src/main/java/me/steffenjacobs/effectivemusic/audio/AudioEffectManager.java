package me.steffenjacobs.effectivemusic.audio;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
public class AudioEffectManager implements FadeCompleteListener {

	private AtomicBoolean fading = new AtomicBoolean(false);
	private Fade fade;

	public void fadeTo(final double targetGain, long fadeLengthInMillis, AudioPlayer audioPlayer) {
		fadeTo(targetGain, fadeLengthInMillis, null, audioPlayer);
	}

	public void fadeTo(final double targetGain, long fadeLengthInMillis, FadeCompleteListener onComplete, AudioPlayer audioPlayer) {

		final double initialGain = audioPlayer.getGain();

		if (targetGain == initialGain && !fading.get()) {
			return;
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
		fadeIP.setAudioPlayer(audioPlayer);
		fadeIP.setFadeUp(targetGain - initialGain > 0);
		if (onComplete != null) {
			fadeIP.addListener(onComplete);
		}

		if (fading.get() && fade != null) {
			fade.resumeFade();
		} else {
			fade = fadeIP;
			fading.set(true);
			fade.runAsync();
		}
	}

	public void fadeOut(int length, AudioPlayer audioPlayer) {
		fadeTo(0, length, audioPlayer);
	}

	@Override
	public void onFadeComplete() {
		fading.set(false);
	}
}
