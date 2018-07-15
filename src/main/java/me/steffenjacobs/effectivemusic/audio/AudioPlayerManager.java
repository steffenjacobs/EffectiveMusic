package me.steffenjacobs.effectivemusic.audio;

import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
public class AudioPlayerManager {
	
	@Autowired
	AudioPlayer vlcPlayer;

	public void playAudio(TrackMetadata metadata) {
		vlcPlayer.playAudio(metadata);
	}

	public void stop() {
		vlcPlayer.stop();
	}

	public void pause() {
		vlcPlayer.pause();
	}

	public void resume() {
		vlcPlayer.resume();
	}

	public Status getStatus() {
		return vlcPlayer.getStatus();
	}

	public double getGain() {
		return vlcPlayer.getGain();
	}

	public void setGain(double value) {
		vlcPlayer.setGain(value);
	}

	public float getPosition() {
		return vlcPlayer.getPosition();
	}

	public void setPosition(float position) {
		vlcPlayer.setPosition(position);
	}

	public TrackDTO getTrackInformation() throws TagException {
		return vlcPlayer.getTrackInformation();
	}

	public long getLength() {
		return vlcPlayer.getLength();
	}

	public void addListener(AudioPlayerListener listener) {
		vlcPlayer.addListener(listener);
	}

	public AudioPlayer getCurrentAudioPlayer() {
		return vlcPlayer;
	}

}
