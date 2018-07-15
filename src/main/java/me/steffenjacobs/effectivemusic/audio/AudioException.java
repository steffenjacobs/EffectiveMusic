package me.steffenjacobs.effectivemusic.audio;

/** @author Steffen Jacobs */
public class AudioException extends RuntimeException {
	private static final long serialVersionUID = 4462080080793360145L;

	public AudioException() {
		super();
	}

	public AudioException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	public AudioException(Throwable t) {
		super(t);
	}
}
