package me.steffenjacobs.effectivemusic.audio.dual;

import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import me.steffenjacobs.effectivemusic.audio.AudioPlayer;
import me.steffenjacobs.effectivemusic.audio.VLCMediaPlayerAdapter;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;

/** @author Steffen Jacobs */
// @Component("dualPlayer")
// @Scope("singleton")
// TODO
public class DualAudioPlayer implements AudioPlayer, InitializingBean {

	@Autowired
	VLCMediaPlayerAdapter player1;

	@Autowired
	VLCMediaPlayerAdapter player2;

	private VLCMediaPlayerAdapter currentPlayer;

	private VLCMediaPlayerAdapter switchPlayer() {
		currentPlayer = getOtherPlayer();
		System.out.println("switched player to " + (currentPlayer == player1 ? "1" : "2"));
		return currentPlayer;
	}

	private VLCMediaPlayerAdapter getOtherPlayer() {
		if (player1.getStatus() != Status.STOPPED) {
			return player1;
		}
		if (player2.getStatus() != Status.STOPPED) {
			return player2;
		}
		return player1;
	}

	@Override
	public void playAudio(String path) {
		currentPlayer.stop();
		switchPlayer().playAudio(path);
	}

	@Override
	public void stop() {
		currentPlayer.stop();
	}

	@Override
	public void pause() {
		currentPlayer.pause();
	}

	@Override
	public void resume() {
		currentPlayer.resume();
	}

	@Override
	public Status getStatus() {
		return currentPlayer.getStatus();
	}

	@Override
	public double getGain() {
		return currentPlayer.getGain();
	}

	@Override
	public void setGain(double value) {
		player1.setGain(value);
		player2.setGain(value);
	}

	@Override
	public float getPosition() {
		return currentPlayer.getPosition();
	}

	@Override
	public void setPosition(float position) {
		currentPlayer.setPosition(position);
	}

	@Override
	public TrackDTO getTrackInformation() throws TagException {
		return currentPlayer.getTrackInformation();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		currentPlayer = player1;
	}
}
