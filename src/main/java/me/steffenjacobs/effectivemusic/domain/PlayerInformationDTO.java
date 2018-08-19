package me.steffenjacobs.effectivemusic.domain;

/** @author Steffen Jacobs */
public class PlayerInformationDTO {

	private String status;
	private long loopStatus;
	private double volume;
	private boolean mute;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getLoopStatus() {
		return loopStatus;
	}

	public void setLoopStatus(long loopStatus) {
		this.loopStatus = loopStatus;
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
