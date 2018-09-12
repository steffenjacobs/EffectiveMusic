package me.steffenjacobs.effectivemusic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FilenameUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivemusic.audio.AudioPlayer;
import me.steffenjacobs.effectivemusic.audio.AudioPlayerListener;
import me.steffenjacobs.effectivemusic.audio.AudioPlayerManager;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.util.Pair;
import uk.co.caprica.vlcj.player.MediaPlayer;

/** @author Steffen Jacobs */
@Component
public class PlaylistManager {

	public static enum LOOP_STATUS {
		LOOP_ONE(1), LOOP_ALL(2), NO_LOOP(0), SHUFFLE(3);

		private final int value;

		private LOOP_STATUS(int value) {
			this.value = value;
		}

		public static LOOP_STATUS fromValue(int value) {
			switch (value) {
			case 1:
				return LOOP_ONE;
			case 2:
				return LOOP_ALL;
			case 3:
				return SHUFFLE;
			default:
				return NO_LOOP;
			}
		}

		public int getValue() {
			return value;
		}
	}

	private static final long DELAY_NEXT_SONG_MILLIS = 500;
	private static final boolean PLAY_IMMEDIATELY = true;

	private LOOP_STATUS loopStatus = LOOP_STATUS.LOOP_ALL;

	private final List<TrackMetadata> playlist = new ArrayList<>();
	private int currentIndex = 0;

	private AtomicBoolean skip = new AtomicBoolean(false);

	private String playlistName = "Unnamed Playlist";

	@Autowired
	AudioPlayerManager audioPlayerManager;

	@Autowired
	PlaylistStorageService playlistStorageService;

	public void queue(TrackMetadata track) {
		if (track.getTrackDTO() == null) {
			track.setTrackDTO(getTrackInfo(track.getPath()));
		}
		playlist.add(track);
		if (PLAY_IMMEDIATELY && audioPlayerManager.getStatus() == Status.STOPPED) {
			audioPlayerManager.playAudio(track);
		}
	}

	public TrackDTO getTrackInfo(String path) {
		try {
			AudioFile f = AudioFileIO.read(new File(path));
			return new TrackDTO(f.getTag(), f.getAudioHeader().getTrackLength() * 1000);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
			e.printStackTrace();
		}
		return new TrackDTO();
	}

	public boolean dequeue(int index) {
		try {
			playlist.remove(index);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private void playRandomTrackFromPlaylist() {
		currentIndex = (int) (playlist.size() * Math.random());
		audioPlayerManager.playAudio(playlist.get(currentIndex));
	}

	public void playNext() {
		skip.set(true);
		if (loopStatus == LOOP_STATUS.SHUFFLE) {
			playRandomTrackFromPlaylist();
		} else if (currentIndex + 1 <= playlist.size() - 1) {
			audioPlayerManager.playAudio(playlist.get(++currentIndex));
		} else if (loopStatus == LOOP_STATUS.LOOP_ALL) {
			currentIndex = 0;
			audioPlayerManager.playAudio(playlist.get(currentIndex));
		}
		skip.set(false);
	}

	public void playPrevious() {
		skip.set(true);
		if (loopStatus == LOOP_STATUS.SHUFFLE) {
			playRandomTrackFromPlaylist();
		} else if (currentIndex - 1 >= 0) {
			audioPlayerManager.playAudio(playlist.get(--currentIndex));
		} else if (loopStatus == LOOP_STATUS.LOOP_ALL) {
			currentIndex = playlist.size() - 1;
			audioPlayerManager.playAudio(playlist.get(currentIndex));
		}
		skip.set(false);
	}

	public List<TrackMetadata> getPlaylist() {
		return playlist;
	}

	public void setLoopStatus(LOOP_STATUS loopStatus) {
		this.loopStatus = loopStatus;
	}

	public LOOP_STATUS getLoopStatus() {
		return loopStatus;
	}

	public void startPlaylist() {
		if (loopStatus == LOOP_STATUS.SHUFFLE) {
			playRandomTrackFromPlaylist();
		} else {
			currentIndex = 0;
			audioPlayerManager.playAudio(playlist.get(currentIndex));
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void registerListener() {
		final VLCPlayerEventHandler handler = new VLCPlayerEventHandler() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				if (skip.get()) {
					return;
				}

				final boolean l = loopStatus == LOOP_STATUS.LOOP_ONE;
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

	public void playPosition(int position) {
		if (position <= playlist.size() - 1) {
			skip.set(true);
			currentIndex = position;
			audioPlayerManager.playAudio(playlist.get(currentIndex));
			skip.set(false);
		} else {
			throw new IndexOutOfBoundsException("Element #" + position + " does not exist in the current playlist!");
		}
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void createNewPlaylist() {
		playlistName = "Unnamed Playlist";
		playlist.clear();
		currentIndex = -1;
	}

	public void savePlaylist(String path) throws IOException {
		playlistName = FilenameUtils.removeExtension(new File(path).getName());
		playlistStorageService.savePlaylist(path, playlist);
	}

	public void loadPlaylist(String path) throws IOException {
		playlist.clear();
		currentIndex = -1;
		Pair<String, List<String>> list = playlistStorageService.loadPlaylist(path);
		playlistName = list.getX();
		list.getY().forEach(p -> queue(new TrackMetadata(p)));
	}

	public String getPlaylistName() {
		return playlistName;
	}
}
