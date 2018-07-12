package me.steffenjacobs.effectivemusic.audio;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jna.NativeLibrary;

import me.steffenjacobs.effectivemusic.VLCPlayerEventHandler;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/** @author Steffen Jacobs */

@Component("vlcPlayer")
@Scope("singleton")
public class VLCMediaPlayerAdapter implements AudioPlayer {

	private static final Logger LOG = LoggerFactory.getLogger(AudioPlayer.class);

	private static final String NATIVE_LIBRARY_SEARCH_PATH = "L:\\Programme\\VLC";

	private boolean initialized = false;

	private MediaPlayer mediaPlayer;

	private String currentPath = "";

	private Status status = Status.STOPPED;

	private void initIfNecessary() {
		if (!initialized) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
			mediaPlayer = new AudioMediaPlayerComponent().getMediaPlayer();
			mediaPlayer.addMediaPlayerEventListener(new VLCPlayerEventHandler() {
				@Override
				public void finished(MediaPlayer mediaPlayer) {
					status = Status.STOPPED;
					LOG.info("finished.");
				}
			});
			initialized = true;
		}
	}

	public void playFromUrl(URL url) {
		initIfNecessary();
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.playMedia(url.toString());
		status = Status.PLAYING;
	}

	@Override
	public void playAudio(String path) {
		initIfNecessary();
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		LOG.info("playing.");
		mediaPlayer.playMedia(path);
		currentPath = path;
		status = Status.PLAYING;
	}

	@Override
	public void stop() {
		LOG.info("stopped.");
		initIfNecessary();
		mediaPlayer.stop();
		status = Status.STOPPED;
	}

	@Override
	public void pause() {
		LOG.info("paused.");
		initIfNecessary();
		mediaPlayer.pause();
		status = Status.PAUSED;
	}

	@Override
	public void resume() {
		LOG.info("resumed.");
		initIfNecessary();
		mediaPlayer.start();
		status = Status.PLAYING;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public double getGain() {
		initIfNecessary();
		return mediaPlayer.getVolume();
	}

	/**
	 * @param value:
	 *            volume between 0 and 200
	 */
	@Override
	public void setGain(double value) {
		initIfNecessary();
		mediaPlayer.setVolume((int) value);
	}

	@Override
	public float getPosition() {
		initIfNecessary();
		return mediaPlayer.getPosition();
	}

	/**
	 * @param position
	 *            position in the track between 0 and 1
	 */
	@Override
	public void setPosition(float position) {
		initIfNecessary();
		mediaPlayer.setPosition(position);
	}

	@Override
	public TrackDTO getTrackInformation() throws TagException {
		try {
			AudioFile f = AudioFileIO.read(new File(currentPath));
			Tag tag = f.getTag();
			return new TrackDTO(tag);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
			throw new TagException(e);
		}
	}

	public void addListener(MediaPlayerEventListener listener) {
		initIfNecessary();
		mediaPlayer.addMediaPlayerEventListener(listener);
	}

	public long getLength() {
		return mediaPlayer.getLength();
	}

}
