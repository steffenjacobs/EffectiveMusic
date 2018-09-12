package me.steffenjacobs.effectivemusic.domain;

/** @author Steffen Jacobs */
public class SettingsDTO {

	private String playlistLocation;

	public SettingsDTO(String playlistLocation) {
		super();
		this.playlistLocation = playlistLocation;
	}

	public String getPlaylistLocation() {
		return playlistLocation;
	}

	public void setPlaylistLocation(String playlistLocation) {
		this.playlistLocation = playlistLocation;
	}

}
