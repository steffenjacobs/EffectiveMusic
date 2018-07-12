package me.steffenjacobs.effectivemusic.audio;

import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

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

	@Override
	public void playAudio(String path) {
		vlcPlayerAdapter.setGain(0);
		vlcPlayerAdapter.playAudio(path);
		audioEffectManager.fadeTo(gain, FADE_MILLIS, vlcPlayerAdapter);
	}

	@Override
	public void stop() {
		audioEffectManager.fadeTo(0, FADE_MILLIS, (() -> {
			vlcPlayerAdapter.stop();
		}), vlcPlayerAdapter);
	}

	@Override
	public void pause() {
		audioEffectManager.fadeTo(0, FADE_MILLIS, (() -> {
			vlcPlayerAdapter.pause();
		}), vlcPlayerAdapter);
	}

	@Override
	public void resume() {
		vlcPlayerAdapter.setGain(0);
		audioEffectManager.fadeTo(gain, FADE_MILLIS, vlcPlayerAdapter);
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
	public TrackDTO getTrackInformation() throws TagException {
		return vlcPlayerAdapter.getTrackInformation();
	}

	@Override
	public long getLength() {
		return vlcPlayerAdapter.getLength();
	}

	@Override
	public void addListener(MediaPlayerEventListener listener) {
		vlcPlayerAdapter.addListener(listener);
	}
}
