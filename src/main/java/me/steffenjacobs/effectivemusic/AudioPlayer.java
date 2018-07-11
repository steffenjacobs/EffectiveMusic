package me.steffenjacobs.effectivemusic;

import java.io.File;
import java.io.IOException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.util.ImprovedBasicPlayer;

/** @author Steffen Jacobs */
@Component
@Scope("prototype")
public class AudioPlayer {

	static enum Status {
		STOPPED(2), PLAYING(0), PAUSED(1), UNKNOWN(Integer.MIN_VALUE);

		private final int value;

		private Status(int value) {
			this.value = value;
		}

		public static Status fromValue(int value) {
			switch (value) {
			case 0:
				return Status.PLAYING;
			case 1:
				return Status.PAUSED;
			case 2:
				return Status.STOPPED;
			default:
				return Status.UNKNOWN;
			}
		}

		public int getValue() {
			return value;
		}
	}

	private ImprovedBasicPlayer player;

	private double volume = 1;
	private String currentPath = "";

	public void playAudio(String path) throws BasicPlayerException {
		if (player == null) {
			player = new ImprovedBasicPlayer();
		}
		currentPath = path;

		player.open(new File(path));
		player.play();
		player.setGain(volume);
	}

	public void stop() throws BasicPlayerException {
		if (player != null) {
			player.stop();
		}
	}

	public void pause() throws BasicPlayerException {
		if (player != null) {
			player.pause();
		}
	}

	public void resume() throws BasicPlayerException {
		if (player != null) {
			player.resume();
		}
	}

	public Status getStatus() {
		if (player != null) {
			return Status.fromValue(player.getStatus());
		}
		return Status.UNKNOWN;
	}

	public double getGain() {
		return volume;
	}

	public void setGain(double value) throws BasicPlayerException {
		volume = value;
		if (player != null) {
			player.setGain(value);
		}
	}

	public long getFramePosition() {
		return player.getFramePosition();
	}

	public long getMicrosecondPosition() {
		return player.getMicrosecondPosition();
	}

	public void setPosition(long position) throws BasicPlayerException {
		player.seek(position);
	}
}
