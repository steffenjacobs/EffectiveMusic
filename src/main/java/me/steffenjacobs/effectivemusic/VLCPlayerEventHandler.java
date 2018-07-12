package me.steffenjacobs.effectivemusic;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/** @author Steffen Jacobs */
public class VLCPlayerEventHandler implements MediaPlayerEventListener{

	@Override
	public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opening(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buffering(MediaPlayer mediaPlayer, float newCache) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playing(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paused(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopped(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forward(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backward(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finished(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementaryStreamAdded(MediaPlayer mediaPlayer, int type, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementaryStreamDeleted(MediaPlayer mediaPlayer, int type, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementaryStreamSelected(MediaPlayer mediaPlayer, int type, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void corked(MediaPlayer mediaPlayer, boolean corked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void muted(MediaPlayer mediaPlayer, boolean muted) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mediaFreed(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mediaSubItemTreeAdded(MediaPlayer mediaPlayer, libvlc_media_t item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newMedia(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endOfSubItems(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

}
