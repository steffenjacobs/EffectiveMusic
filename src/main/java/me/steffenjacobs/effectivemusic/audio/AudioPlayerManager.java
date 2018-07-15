package me.steffenjacobs.effectivemusic.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;

/** @author Steffen Jacobs */
@Component
@Scope("singleton")
public class AudioPlayerManager implements InitializingBean {

	@Autowired
	AudioPlayer vlcPlayer;

	@Autowired
	JavazoomAudioPlayer javazoomAudioPlayer;

	private AudioPlayer currentPlayer;

	private TrackMetadata currentlyPlayed;

	private List<AudioPlayerListener> listeners = new ArrayList<>();

	private AtomicBoolean ignoreNextStopFinishEvent = new AtomicBoolean(false);

	public void playAudio(TrackMetadata metadata) {
		currentlyPlayed = metadata;
		try {
			AudioFile f = AudioFileIO.read(new File(currentlyPlayed.getPath()));
			AudioHeader header = f.getAudioHeader();
			stopSilent();

			if (header.getFormat().startsWith("FLAC")) {
				currentlyPlayed = javazoomAudioPlayer.playAudio(metadata);
				currentPlayer = javazoomAudioPlayer;
			} else {
				currentlyPlayed = vlcPlayer.playAudio(metadata);
				currentPlayer = vlcPlayer;
			}
			currentlyPlayed.setTrackDTO(new TrackDTO(f.getTag(), f.getAudioHeader().getTrackLength()));
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {

			stopSilent();
			currentlyPlayed = vlcPlayer.playAudio(metadata);
			currentPlayer = vlcPlayer;
		}
	}

	private void stopSilent() {
		if (getCurrentAudioPlayer().getStatus() == Status.PLAYING) {
			ignoreNextStopFinishEvent.set(true);
			getCurrentAudioPlayer().stop();
		}
	}

	public void stop() {
		vlcPlayer.stop();
	}

	public void pause() {
		vlcPlayer.pause();
	}

	public void resume() {
		vlcPlayer.resume();
	}

	public Status getStatus() {
		return vlcPlayer.getStatus();
	}

	public double getGain() {
		return vlcPlayer.getGain();
	}

	public void setGain(double value) {
		vlcPlayer.setGain(value);
	}

	public float getPosition() {
		return vlcPlayer.getPosition();
	}

	public void setPosition(float position) {
		vlcPlayer.setPosition(position);
	}

	public TrackDTO getTrackInformation() throws TagException {
		return currentlyPlayed.getTrackDTO();
	}

	public void addListener(AudioPlayerListener listener) {
		listeners.add(listener);
	}

	public AudioPlayer getCurrentAudioPlayer() {
		return currentPlayer;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		final AudioPlayerListener listener = new AudioPlayerListener() {
			@Override
			public void onStart() {
				listeners.forEach(l -> l.onStart());
			}

			@Override
			public void onStop() {
				if (ignoreNextStopFinishEvent.getAndSet(false)) {
					listeners.forEach(l -> l.onStop());
				}
			}

			@Override
			public void onPause() {
				listeners.forEach(l -> l.onPause());
			}

			@Override
			public void onResume() {
				listeners.forEach(l -> l.onResume());
			}

			@Override
			public void onFinish() {
				if (ignoreNextStopFinishEvent.getAndSet(false)) {
					listeners.forEach(l -> l.onFinish());
				}
			}
		};
		vlcPlayer.addListener(listener);
		javazoomAudioPlayer.addListener(listener);
		currentPlayer = vlcPlayer;
	}

}
