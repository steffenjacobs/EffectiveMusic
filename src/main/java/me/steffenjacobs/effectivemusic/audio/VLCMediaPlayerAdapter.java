package me.steffenjacobs.effectivemusic.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.VLCPlayerEventHandler;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;

/** @author Steffen Jacobs */

@Component("vlcPlayerAdapter")
@Scope("singleton")
public class VLCMediaPlayerAdapter implements AudioPlayer, InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(AudioPlayer.class);

	private MediaPlayer mediaPlayer;

	private TrackMetadata currentlyPlayed;

	private Status status = Status.STOPPED;

	@Override
	public TrackMetadata playAudio(TrackMetadata metadata) {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		currentlyPlayed = metadata;
		mediaPlayer.playMedia(metadata.getPath());
		status = Status.PLAYING;
		LOG.info("playing " + metadata != null && metadata.getTrackDTO() != null ? metadata.getTrackDTO().getTitle() : "");
		return currentlyPlayed;
	}

	@Override
	public void stop() {
		LOG.info("stopped.");
		mediaPlayer.stop();
		status = Status.STOPPED;
	}

	@Override
	public void pause() {
		LOG.info("paused.");
		mediaPlayer.pause();
		status = Status.PAUSED;
	}

	@Override
	public void resume() {
		LOG.info("resumed.");
		mediaPlayer.start();
		status = Status.PLAYING;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public double getGain() {
		return mediaPlayer.getVolume();
	}

	/**
	 * ! ATTENTION: THIS FUNCTION WORKS ASNYC!
	 * 
	 * @param value
	 *            volume between 0 and 200
	 */
	@Override
	public void setGain(double value) {
		mediaPlayer.setVolume((int) value);
	}

	@Override
	public float getPosition() {
		return mediaPlayer.getPosition();
	}

	/**
	 * @param position
	 *            position in the track between 0 and 1
	 */
	@Override
	public void setPosition(float position) {
		mediaPlayer.setPosition(position);
	}

	@Override
	public void addListener(final AudioPlayerListener listener) {
		mediaPlayer.addMediaPlayerEventListener(new VLCPlayerEventHandler() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				listener.onFinish();
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try{
		LOG.info(new NativeDiscovery().discover() ? "Library found" : "No library found!");
		mediaPlayer = new AudioMediaPlayerComponent().getMediaPlayer();
		mediaPlayer.addMediaPlayerEventListener(new VLCPlayerEventHandler() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				status = Status.STOPPED;
				LOG.info("finished playback.");
			}

			@Override
			public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
				currentlyPlayed.getTrackDTO().setLength(mediaPlayer.getLength());
			}
		});
		}
		catch(UnsatisfiedLinkError e){
			LOG.error("Could not load VLC4J Library: {}", e.getMessage());
		}
	}

	@Override
	public boolean isMute() {
		return mediaPlayer.isMute();
	}

	@Override
	public void setMute(boolean mute) {
		mediaPlayer.mute(mute);
	}
}
