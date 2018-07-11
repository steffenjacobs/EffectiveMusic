package me.steffenjacobs.effectivemusic;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

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

	private BasicPlayer player;

	public void playAudio(String path) throws BasicPlayerException {
		if (player == null) {
			player = new BasicPlayer();
		}

		try {
			player.open(new URL("file:///" + path));
		} catch (MalformedURLException e) {
			throw new BasicPlayerException(e);
		}
		player.play();
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
}
