package me.steffenjacobs.effectivemusic.domain;

/** @author Steffen Jacobs */
public class TrackMetadata {

	private TrackDTO trackDTO;
	private String path;

	public TrackMetadata() {

	}
	
	public TrackMetadata(String path) {
		this.path = path;
	}

	public TrackDTO getTrackDTO() {
		return trackDTO;
	}

	public void setTrackDTO(TrackDTO trackDTO) {
		this.trackDTO = trackDTO;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
