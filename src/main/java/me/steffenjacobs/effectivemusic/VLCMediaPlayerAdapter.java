package me.steffenjacobs.effectivemusic;

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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jna.NativeLibrary;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/** @author Steffen Jacobs */

@Component
@Scope("singleton")
public class VLCMediaPlayerAdapter implements AudioPlayer {

	private static final String NATIVE_LIBRARY_SEARCH_PATH = "L:\\Programme\\VLC";

	private boolean initialized = false;

	private MediaPlayer mediaPlayer;

	private String currentPath = "";

	private Status status = Status.UNKNOWN;

	private void initIfNecessary() {
		if (!initialized) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
			mediaPlayer = new AudioMediaPlayerComponent().getMediaPlayer();
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
	public void playAudio(String path) throws BasicPlayerException {
		initIfNecessary();
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.playMedia(path);
		currentPath = path;
		status = Status.PLAYING;
	}

	@Override
	public void stop() throws BasicPlayerException {
		initIfNecessary();
		mediaPlayer.stop();
		status = Status.STOPPED;
	}

	@Override
	public void pause() throws BasicPlayerException {
		initIfNecessary();
		mediaPlayer.pause();
		status = Status.PAUSED;
	}

	@Override
	public void resume() throws BasicPlayerException {
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

	@Override
	public void setGain(double value) throws BasicPlayerException {
		initIfNecessary();
		mediaPlayer.setVolume((int) value);
	}

	@Override
	public long getFramePosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMicrosecondPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPosition(long position) throws BasicPlayerException {
		// TODO Auto-generated method stub
	}

	@Override
	public TrackDTO getTrackInformation() throws BasicPlayerException {
		try {
			AudioFile f = AudioFileIO.read(new File(currentPath));
			Tag tag = f.getTag();
			return new TrackDTO(tag);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
			throw new BasicPlayerException(e);
		}
	}

}
