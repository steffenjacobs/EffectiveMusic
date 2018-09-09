package me.steffenjacobs.effectivemusic.audio;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

import me.steffenjacobs.effectivemusic.OutgoingStatisticsService;
import me.steffenjacobs.effectivemusic.domain.LiveTrackDTO;
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
	
	@Autowired
	OutgoingStatisticsService outgoingStatisticsService;

	private AudioPlayer currentPlayer;

	private TrackMetadata currentlyPlayed;

	private List<AudioPlayerListener> listeners = new CopyOnWriteArrayList<>();

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
			currentlyPlayed.setTrackDTO(new TrackDTO(f.getTag(), header.getTrackLength() * 1000));
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {

			stopSilent();
			currentlyPlayed = vlcPlayer.playAudio(metadata);
			currentPlayer = vlcPlayer;
		}
		outgoingStatisticsService.sendUpdateStatisticInfoIfAvailable(metadata);
	}

	private void stopSilent() {
		if (getCurrentAudioPlayer().getStatus() == Status.PLAYING) {
			ignoreNextStopFinishEvent.set(true);
			getCurrentAudioPlayer().stop();
		}
		currentlyPlayed = null;
	}

	public void stop() {
		if (getCurrentAudioPlayer().getStatus() == Status.PLAYING) {
			getCurrentAudioPlayer().stop();
		}
		currentlyPlayed = null;
	}

	public void pause() {
		currentPlayer.pause();
	}

	public void resume() {
		currentPlayer.resume();
	}

	public Status getStatus() {
		return currentPlayer.getStatus();
	}

	public double getGain() {
		return currentPlayer.getGain();
	}

	public void setGain(double value) {
		currentPlayer.setGain(value);
	}

	public float getPosition() {
		return currentPlayer.getPosition();
	}

	public void setPosition(float position) {
		currentPlayer.setPosition(position);
	}

	public TrackDTO getTrackInformation() {
		return currentlyPlayed != null ? currentlyPlayed.getTrackDTO() : null;
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
				if (!ignoreNextStopFinishEvent.getAndSet(false)) {
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
				if (!ignoreNextStopFinishEvent.getAndSet(false)) {
					listeners.forEach(AudioPlayerListener::onFinish);
				}
			}
		};
		vlcPlayer.addListener(listener);
		javazoomAudioPlayer.addListener(listener);
		currentPlayer = vlcPlayer;
	}

	public LiveTrackDTO getLiveTrackInformation() {
		LiveTrackDTO dto = new LiveTrackDTO(getTrackInformation());
		dto.setPosition(getPosition());
		dto.setVolume(getGain());
		return dto;
	}

}
