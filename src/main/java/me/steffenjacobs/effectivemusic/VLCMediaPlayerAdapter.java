package me.steffenjacobs.effectivemusic;

import java.net.URL;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/** @author Steffen Jacobs */

@Component
@Scope("singleton")
public class VLCMediaPlayerAdapter {

	private static final String NATIVE_LIBRARY_SEARCH_PATH = "L:\\Programme\\VLC";

	private boolean initialized = false;

	private MediaPlayer mediaPlayer;

	private void initIfNecessary() {
		if (!initialized) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
			mediaPlayer = new AudioMediaPlayerComponent().getMediaPlayer();

		}
	}

	public void playFromUrl(URL url) {
		initIfNecessary();
		mediaPlayer.playMedia(url.toString());
	}

}
