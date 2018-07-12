package me.steffenjacobs.effectivemusic.audio;

import org.jaudiotagger.tag.TagException;

import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/** @author Steffen Jacobs */
public interface AudioPlayer {

	void playAudio(String path);

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

	void addListener(MediaPlayerEventListener listener);

}
