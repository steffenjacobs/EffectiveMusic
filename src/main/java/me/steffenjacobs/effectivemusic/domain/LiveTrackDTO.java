package me.steffenjacobs.effectivemusic.domain;

/** @author Steffen Jacobs */
public class LiveTrackDTO extends TrackDTO {

	private double position, volume;
	private boolean mute;

	public LiveTrackDTO(TrackDTO copy) {
		if (copy == null) {
			return;
		}
		artist = copy.getArtist();
		album = copy.getAlbum();
		title = copy.getTitle();
		comment = copy.getComment();
		year = copy.getYear();
		track = copy.getTrack();
		disc_no = copy.getDisc_no();
		composer = copy.getComposer();
		artist_sort = copy.getArtist_sort();
		length = copy.getLength();
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

}
