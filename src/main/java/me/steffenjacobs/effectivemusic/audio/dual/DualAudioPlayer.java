package me.steffenjacobs.effectivemusic.audio.dual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import me.steffenjacobs.effectivemusic.audio.AudioPlayer;
import me.steffenjacobs.effectivemusic.audio.AudioPlayerListener;
import me.steffenjacobs.effectivemusic.audio.VLCMediaPlayerAdapter;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;

/** @author Steffen Jacobs */
// @Component("dualPlayer")
// @Scope("singleton")
// TODO
public class DualAudioPlayer implements AudioPlayer, InitializingBean {

	private static Logger LOG = LoggerFactory.getLogger(DualAudioPlayer.class);

	@Autowired
	VLCMediaPlayerAdapter player1;

	@Autowired
	VLCMediaPlayerAdapter player2;

	private VLCMediaPlayerAdapter currentPlayer;

	private VLCMediaPlayerAdapter switchPlayer() {
		currentPlayer = getOtherPlayer();
		LOG.info("switched player to {}", (currentPlayer == player1 ? "1" : "2"));
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
	public TrackMetadata playAudio(TrackMetadata metadata) {
		currentPlayer.stop();
		switchPlayer().playAudio(metadata);
		return metadata;
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
	public void afterPropertiesSet() throws Exception {
		currentPlayer = player1;
	}

	@Override
	public void addListener(AudioPlayerListener listener) {
		player1.addListener(listener);
		player2.addListener(listener);
	}

	@Override
	public boolean isMute() {
		return currentPlayer.isMute();
	}

	@Override
	public void setMute(boolean mute) {
		player1.setMute(mute);
		player2.setMute(mute);
	}
}
