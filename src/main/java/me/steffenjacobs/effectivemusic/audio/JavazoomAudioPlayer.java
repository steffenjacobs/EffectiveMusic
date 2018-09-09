package me.steffenjacobs.effectivemusic.audio;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.util.ImprovedBasicPlayer;
import me.steffenjacobs.effectivemusic.util.Normalizer;
import me.steffenjacobs.effectivemusic.util.Normalizer.Interval;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
public class JavazoomAudioPlayer implements AudioPlayer, InitializingBean {

	private ImprovedBasicPlayer player;
	private double volume = 100;
	private TrackMetadata metadata;

	private AtomicBoolean suppressEvent = new AtomicBoolean(false);
	private boolean mute = false;

	@Override
	public TrackMetadata playAudio(TrackMetadata metadata) throws AudioException {
		this.metadata = metadata;
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
		return (float) Normalizer.mapToZeroToOne(player.getFramePosition(), new Interval(0, player.getFrameCount()));
	}

	@Override
	public void setPosition(float position) throws AudioException {
		try {
			long pos = (long) Normalizer.mapZeroToOneToX(position, new Interval(0, player.getFrameCount()));
			if (player.setFramePosition(pos)) {
				suppressEvent.set(true);

				AtomicBoolean once = new AtomicBoolean(false);
				final BasicPlayerListener l = new DefaultBasicPlayerListener() {

					@Override
					public void stateUpdated(BasicPlayerEvent event) {
						switch (event.getCode()) {
						case BasicPlayerEvent.STOPPED:
							if (!once.getAndSet(true)) {
								playAudio(metadata);
							}
							break;
						case BasicPlayerEvent.PLAYING:
							new Timer().schedule(new TimerTask() {
								@Override
								public void run() {
									try {
										player.setFramePosition(pos);
										suppressEvent.set(false);
									} catch (BasicPlayerException e) {
										e.printStackTrace();
									}
								}
							}, 1000);

							player.removeBasicPlayerListener(this);
							break;
						case BasicPlayerEvent.PAUSED:
							break;
						case BasicPlayerEvent.RESUMED:
							break;
						}
					}
				};
				player.addBasicPlayerListener(l);
				player.stop();
			}
		} catch (BasicPlayerException e) {
			throw new AudioException(e);
		}
	}

	@Override
	public void addListener(final AudioPlayerListener listener) {
		player.addBasicPlayerListener(new DefaultBasicPlayerListener() {

			@Override
			public void stateUpdated(BasicPlayerEvent event) {
				if (suppressEvent.get()) {
					return;
				}
				switch (event.getCode()) {
				case BasicPlayerEvent.STOPPED:
					listener.onStop();
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
				case BasicPlayerEvent.EOM:
					listener.onFinish();
					break;
				}
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		player = new ImprovedBasicPlayer();
	}

	@Override
	public boolean isMute() {
		return mute;
	}

	@Override
	public void setMute(boolean mute) {
		if(this.mute && !mute){
			this.mute = false;
			try {
				player.setGain(this.volume);
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
		else if (!this.mute && mute){
			this.mute = true;
			try {
				player.setGain(0);
			} catch (BasicPlayerException e) {
				e.printStackTrace();
			}
		}
	}
}
