package me.steffenjacobs.effectivemusic.youtube;

/** @author Steffen Jacobs */
public class YoutubeNotAvailableException extends RuntimeException{
	private static final long serialVersionUID = -1035847285366533715L;

	public YoutubeNotAvailableException(String url) {
		super(url + " could not be reached.");
	}
}
