package me.steffenjacobs.effectivemusic;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import me.steffenjacobs.effectivemusic.PlaylistManager.LOOP_STATUS;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.domain.TrackMetadataList;
import me.steffenjacobs.effectivemusic.util.Base64Service;
import me.steffenjacobs.effectivemusic.youtube.YoutubeManager;
import me.steffenjacobs.effectivemusic.youtube.YoutubeNotAvailableException;

/** @author Steffen Jacobs */
@Controller
public class PlaylistController {

	@Autowired
	PlaylistManager playlistManager;

	@Autowired
	YoutubeManager youtubeManager;

	@Autowired
	Base64Service base64Service;

	@PostMapping(value = "/music/playlist/enquene", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> enquene(String path) throws MalformedURLException, YoutubeNotAvailableException {
		if (base64Service.isBase64(path)) {
			path = base64Service.decode(path);
		}
		if (path.startsWith("https://www.youtube.com/watch?v=")) {
			playlistManager.queue(youtubeManager.getPlaybackUrl(path));
		} else {
			playlistManager.queue(new TrackMetadata(path));
		}
		return new ResponseEntity<String>("Added file to playlist: " + path, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/music/playlist/dequene", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> dequene(int index) {
		boolean val = playlistManager.dequeue(index);
		return new ResponseEntity<String>("Removed file from playlist: " + index + " - " + val, HttpStatus.ACCEPTED);
	}

	/** no loop: 0, loop once: 1, loop all: 2, shuffle: 3; */
	@PostMapping(value = "/music/playlist/loop", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setLoopAll(int value) {
		playlistManager.setLoopStatus(LOOP_STATUS.fromValue(value));
		return new ResponseEntity<String>("set loop-all to " + LOOP_STATUS.fromValue(value), HttpStatus.OK);
	}

	@GetMapping(value = "/music/playlist/loop")
	public ResponseEntity<Integer> getLoop() {
		return new ResponseEntity<Integer>(playlistManager.getLoopStatus().getValue(), HttpStatus.OK);
	}

	@PostMapping(value = "/music/playlist/next")
	public ResponseEntity<String> next() {
		playlistManager.playNext();
		return new ResponseEntity<String>("Skipped to next track", HttpStatus.OK);
	}

	@PostMapping(value = "/music/playlist/previous")
	public ResponseEntity<String> previous() {
		playlistManager.playPrevious();
		return new ResponseEntity<String>("Skipped to previous track", HttpStatus.OK);
	}

	@PostMapping(value = "/music/playlist/play")
	public ResponseEntity<String> play() {
		playlistManager.startPlaylist();
		return new ResponseEntity<String>("started playlist", HttpStatus.OK);
	}

	@PostMapping(value = "/music/playlist/position", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> gotoPosition(int position) {
		playlistManager.playPosition(position);
		return new ResponseEntity<String>("jumped to track #" + position, HttpStatus.OK);
	}

	@GetMapping(value = "/music/playlist")
	public ResponseEntity<TrackMetadataList> getPlaylist() {
		return new ResponseEntity<TrackMetadataList>(new TrackMetadataList(playlistManager.getPlaylist(), playlistManager.getLoopStatus().getValue(), playlistManager.getCurrentIndex()), HttpStatus.OK);
	}

}
