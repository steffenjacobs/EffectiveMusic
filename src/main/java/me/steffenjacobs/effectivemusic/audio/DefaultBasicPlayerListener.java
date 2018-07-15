package me.steffenjacobs.effectivemusic.audio;

import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

/** @author Steffen Jacobs */
public class DefaultBasicPlayerListener implements BasicPlayerListener {

	@Override
	public void opened(Object stream, @SuppressWarnings("rawtypes") Map properties) {
	}

	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata, @SuppressWarnings("rawtypes") Map properties) {
	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
	}

	@Override
	public void setController(BasicController controller) {
	}
}
