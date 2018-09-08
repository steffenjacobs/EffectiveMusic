package me.steffenjacobs.effectivemusic.audio;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.util.Wrapper;

/** @author Steffen Jacobs */
@Component("vlcPlayer")
@Scope("singleton")
public class SmoothedAudioPlayer implements AudioPlayer {

	private static final long FADE_MILLIS = 400;

	@Autowired
	private VLCMediaPlayerAdapter vlcPlayerAdapter;

	@Autowired
	private AudioEffectManager audioEffectManager;

	private double gain = 100;

	private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

	@Override
	public TrackMetadata playAudio(final TrackMetadata metadata) {
		gain = vlcPlayerAdapter.getGain();
		Wrapper<TrackMetadata> meta = new Wrapper<>();
		vlcPlayerAdapter.setGain(0);
		executor.schedule(() -> {
			meta.setValue(vlcPlayerAdapter.playAudio(metadata));
			audioEffectManager.fadeTo(gain, FADE_MILLIS, vlcPlayerAdapter);
		}, 500, TimeUnit.MILLISECONDS);
		try {
			executor.awaitTermination(2, TimeUnit.SECONDS);
			return meta.getValue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void stop() {
		audioEffectManager.fadeTo(0, FADE_MILLIS, (() -> {
			vlcPlayerAdapter.stop();
			vlcPlayerAdapter.setGain(gain);
		}), vlcPlayerAdapter);
	}

	@Override
	public void pause() {
		audioEffectManager.fadeTo(0, FADE_MILLIS, (() -> {
			vlcPlayerAdapter.pause();
			vlcPlayerAdapter.setGain(gain);
		}), vlcPlayerAdapter);
	}

	@Override
	public void resume() {
		vlcPlayerAdapter.setGain(0);
		executor.schedule(() -> {
			audioEffectManager.fadeTo(gain, FADE_MILLIS, vlcPlayerAdapter);
		}, 500, TimeUnit.MILLISECONDS);
		vlcPlayerAdapter.resume();
	}

	@Override
	public Status getStatus() {
		return vlcPlayerAdapter.getStatus();
	}

	@Override
	public double getGain() {
		return vlcPlayerAdapter.getGain();
	}

	@Override
	public void setGain(double value) {
		gain = value;
		audioEffectManager.fadeTo(value, FADE_MILLIS, vlcPlayerAdapter);
	}

	@Override
	public float getPosition() {
		return vlcPlayerAdapter.getPosition();
	}

	@Override
	public void setPosition(float position) {
		vlcPlayerAdapter.setPosition(position);
	}

	@Override
	public void addListener(AudioPlayerListener listener) {
		vlcPlayerAdapter.addListener(listener);
	}
}
