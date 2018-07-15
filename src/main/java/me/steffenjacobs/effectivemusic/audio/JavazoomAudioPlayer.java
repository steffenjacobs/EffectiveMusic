package me.steffenjacobs.effectivemusic.audio;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.util.ImprovedBasicPlayer;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
public class JavazoomAudioPlayer implements AudioPlayer, InitializingBean {

	private ImprovedBasicPlayer player;

	private double volume = 1;

	@Override
	public TrackMetadata playAudio(TrackMetadata metadata) throws AudioException {
		
		try {
			player.open(new File(metadata.getPath()));
			player.play();
			player.setGain(volume);
			return metadata;
		} catch (BasicPlayerException e) {
			throw new AudioException(e);
		}
	}

	@Override
	public void stop() throws AudioException {
		try {
			player.stop();
		} catch (BasicPlayerException e) {
			throw new AudioException(e);
		}
	}

	@Override
	public void pause() throws AudioException {
		try {
			player.pause();
		} catch (BasicPlayerException e) {
			throw new AudioException(e);
		}
	}

	@Override
	public void resume() throws AudioException {
		try {
			player.resume();
		} catch (BasicPlayerException e) {
			throw new AudioException(e);
		}
	}

	@Override
	public Status getStatus() {
		return Status.fromValue(player.getStatus());
	}

	@Override
	public double getGain() {
		return volume;
	}

	@Override
	public void setGain(double value) throws AudioException {
		volume = value;
		try {
			player.setGain(value);
		} catch (BasicPlayerException e) {
			throw new AudioException(e);
		}
	}

	// @Override
	public long getFramePosition() {
		return player.getFramePosition();
	}

	// @Override
	public long getMicrosecondPosition() {
		return player.getMicrosecondPosition();
	}

	// @Override
	public void setPosition(long position) throws AudioException {
		try {
			player.seek(position);
		} catch (BasicPlayerException e) {
			throw new AudioException(e);
		}
	}

	@Override
	public float getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPosition(float position) throws AudioException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addListener(final AudioPlayerListener listener) {
		player.addBasicPlayerListener(new BasicPlayerListener() {

			@Override
			public void stateUpdated(BasicPlayerEvent event) {
				switch (event.getCode()) {
				case BasicPlayerEvent.STOPPED:
					listener.onStop();
					listener.onFinish();
					break;
				case BasicPlayerEvent.PLAYING:
					listener.onStart();
					break;
				case BasicPlayerEvent.PAUSED:
					listener.onPause();
					break;
				case BasicPlayerEvent.RESUMED:
					listener.onResume();
					break;
				}
			}

			@Override
			public void setController(BasicController controller) {
			}

			@Override
			public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
			}

			@Override
			public void opened(Object stream, Map properties) {
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		player = new ImprovedBasicPlayer();
	}
}
