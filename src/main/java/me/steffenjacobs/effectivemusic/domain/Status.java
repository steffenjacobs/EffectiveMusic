package me.steffenjacobs.effectivemusic.domain;

/** @author Steffen Jacobs */
public enum Status {
	STOPPED(2), PLAYING(0), PAUSED(1), UNKNOWN(Integer.MIN_VALUE);

	private final int value;

	private Status(int value) {
		this.value = value;
	}

	public static Status fromValue(int value) {
		switch (value) {
		case 0:
			return Status.PLAYING;
		case 1:
			return Status.PAUSED;
		case 2:
			return Status.STOPPED;
		default:
			return Status.UNKNOWN;
		}
	}

	public int getValue() {
		return value;
	}
}
