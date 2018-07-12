package me.steffenjacobs.effectivemusic;

import java.io.File;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.util.ImprovedBasicPlayer;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
public class JavazoomAudioPlayer implements AudioPlayer {

	private ImprovedBasicPlayer player;

	private double volume = 1;
	private String currentPath = "";

	@Override
	public void playAudio(String path) throws BasicPlayerException {
		if (player == null) {
			player = new ImprovedBasicPlayer();
		}
		currentPath = path;

		player.open(new File(path));
		player.play();
		player.setGain(volume);
	}

	@Override
	public void stop() throws BasicPlayerException {
		if (player != null) {
			player.stop();
		}
	}

	@Override
	public void pause() throws BasicPlayerException {
		if (player != null) {
			player.pause();
		}
	}

	@Override
	public void resume() throws BasicPlayerException {
		if (player != null) {
			player.resume();
		}
	}

	@Override
	public Status getStatus() {
		if (player != null) {
			return Status.fromValue(player.getStatus());
		}
		return Status.UNKNOWN;
	}

	@Override
	public double getGain() {
		return volume;
	}

	@Override
	public void setGain(double value) throws BasicPlayerException {
		volume = value;
		if (player != null) {
			player.setGain(value);
		}
	}

//	@Override
	public long getFramePosition() {
		return player.getFramePosition();
	}

//	@Override
	public long getMicrosecondPosition() {
		return player.getMicrosecondPosition();
	}

//	@Override
	public void setPosition(long position) throws BasicPlayerException {
		player.seek(position);
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

	@Override
	public float getPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPosition(float position) throws BasicPlayerException {
		// TODO Auto-generated method stub
		
	}
}
