package me.steffenjacobs.effectivemusic.audio;

import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;

/** @author Steffen Jacobs */
public interface AudioPlayer {

	TrackMetadata playAudio(TrackMetadata metadata);

	void stop();

	void pause();

	void resume();

	Status getStatus();

	double getGain();

	void setGain(double value);

	float getPosition();

	void setPosition(float position);

	void addListener(AudioPlayerListener listener);

	boolean isMute();

	void setMute(boolean mute);
}
