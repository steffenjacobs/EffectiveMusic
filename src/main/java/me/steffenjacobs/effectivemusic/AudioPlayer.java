package me.steffenjacobs.effectivemusic;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;

/** @author Steffen Jacobs */
public interface AudioPlayer {

	void playAudio(String path) throws BasicPlayerException;

	void stop() throws BasicPlayerException;

	void pause() throws BasicPlayerException;

	void resume() throws BasicPlayerException;

	Status getStatus();

	double getGain();

	void setGain(double value) throws BasicPlayerException;

	long getFramePosition();

	long getMicrosecondPosition();

	void setPosition(long position) throws BasicPlayerException;

	TrackDTO getTrackInformation() throws BasicPlayerException;

}