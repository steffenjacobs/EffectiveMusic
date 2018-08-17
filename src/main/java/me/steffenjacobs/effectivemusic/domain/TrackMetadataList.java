package me.steffenjacobs.effectivemusic.domain;

import java.util.List;

/** @author Steffen Jacobs */
public class TrackMetadataList {
	List<TrackMetadata> tracks;

	public TrackMetadataList() {
	}

	public TrackMetadataList(List<TrackMetadata> tracks) {
		this.tracks = tracks;
	}

	public void setTracks(List<TrackMetadata> tracks) {
		this.tracks = tracks;
	}

	public List<TrackMetadata> getTracks() {
		return tracks;
	}

}
