package me.steffenjacobs.effectivemusic.audio;

import org.jaudiotagger.tag.TagException;

import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;

/** @author Steffen Jacobs */
public interface AudioPlayer {

	void playAudio(TrackMetadata metadata);

	void stop();

	void pause();

	void resume();

	Status getStatus();

	double getGain();

	void setGain(double value);

	float getPosition();

	void setPosition(float position);

	TrackDTO getTrackInformation() throws TagException;

	long getLength();

	void addListener(AudioPlayerListener listener);

}
