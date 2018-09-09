package me.steffenjacobs.effectivemusic.domain;

import java.util.List;

/** @author Steffen Jacobs */
public class TrackMetadataList {
	List<TrackMetadata> tracks;

	int repeatLoopStatus;

	int currentIndex;

	public TrackMetadataList() {
	}

	public TrackMetadataList(List<TrackMetadata> tracks, int repeatLoopStatus, int currentIndex) {
		this.tracks = tracks;
		this.repeatLoopStatus = repeatLoopStatus;
		this.currentIndex = currentIndex;
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
