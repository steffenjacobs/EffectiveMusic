package me.steffenjacobs.effectivemusic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.audio.AudioPlayer;
import me.steffenjacobs.effectivemusic.audio.AudioPlayerListener;
import me.steffenjacobs.effectivemusic.audio.AudioPlayerManager;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import uk.co.caprica.vlcj.player.MediaPlayer;

/** @author Steffen Jacobs */
@Component
public class PlaylistManager {

	private static final long DELAY_NEXT_SONG_MILLIS = 500;
	private static final boolean PLAY_IMMEDIATELY = true;

	private boolean loopAll = true, loopOne = false;

	private final List<TrackMetadata> playlist = new ArrayList<>();
	private int currentIndex = 0;

	private AtomicBoolean skip = new AtomicBoolean(false);

	@Autowired
	AudioPlayerManager audioPlayerManager;

	public void queue(TrackMetadata track) {
		playlist.add(track);
		if (PLAY_IMMEDIATELY && audioPlayerManager.getStatus() == Status.STOPPED) {
			audioPlayerManager.playAudio(track);
		}
	}

	public boolean dequeue(int index) {
		try {
			playlist.remove(index);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public void playNext() {
		skip.set(true);
		if (currentIndex + 1 <= playlist.size() - 1) {
			audioPlayerManager.playAudio(playlist.get(++currentIndex));
		} else if (loopAll) {
			currentIndex = 0;
			audioPlayerManager.playAudio(playlist.get(currentIndex));
		}
		skip.set(false);
	}

	public void playPrevious() {
		skip.set(true);
		if (currentIndex - 1 >= 0) {
			audioPlayerManager.playAudio(playlist.get(--currentIndex));
		} else if (loopAll) {
			currentIndex = playlist.size() - 1;
			audioPlayerManager.playAudio(playlist.get(currentIndex));
		}
		skip.set(false);
	}

	public List<TrackMetadata> getPlaylist() {
		return playlist;
	}

	public void setLoopAll(boolean value) {
		loopAll = value;
	}

	public void setLoopOne(boolean value) {
		loopOne = value;
	}

	public void startPlaylist() {
		currentIndex = 0;
		audioPlayerManager.playAudio(playlist.get(currentIndex));
	}

	@EventListener(ApplicationReadyEvent.class)
	public void registerListener() {
		final VLCPlayerEventHandler handler = new VLCPlayerEventHandler() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				if (skip.get()) {
					return;
				}

				final boolean l = loopOne;
				final AudioPlayer p = audioPlayerManager.getCurrentAudioPlayer();
				final int i = currentIndex;

				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						if (l) {
							p.playAudio(playlist.get(i));
						} else {
							playNext();
						}
					}
				}, DELAY_NEXT_SONG_MILLIS);
			}
		};
		audioPlayerManager.addListener(new AudioPlayerListener() {

			@Override
			public void onFinish() {
				handler.finished(null);
			}
		});
	}

	public void clearPlaylist() {
		skip.set(true);
		this.playlist.clear();
		audioPlayerManager.stop();
		skip.set(false);
	}
}
