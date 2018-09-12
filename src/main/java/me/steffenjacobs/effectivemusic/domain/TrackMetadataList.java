package me.steffenjacobs.effectivemusic.domain;

import java.util.List;

/** @author Steffen Jacobs */
public class TrackMetadataList {
	List<TrackMetadata> tracks;

	int repeatLoopStatus;

	int currentIndex;

	String playlistName;

	public TrackMetadataList() {
	}

	public TrackMetadataList(List<TrackMetadata> tracks, int repeatLoopStatus, int currentIndex, String playlistName) {
		this.tracks = tracks;
		this.repeatLoopStatus = repeatLoopStatus;
		this.currentIndex = currentIndex;
		this.playlistName = playlistName;
	}

	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

	public void setTracks(List<TrackMetadata> tracks) {
		this.tracks = tracks;
	}

	public List<TrackMetadata> getTracks() {
		return tracks;
	}

	public int getRepeatLoopStatus() {
		return repeatLoopStatus;
	}

	public void setRepeatLoopStatus(int repeatLoopStatus) {
		this.repeatLoopStatus = repeatLoopStatus;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

}
