package me.steffenjacobs.effectivemusic;

import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.youtube.YoutubeManager;

/** @author Steffen Jacobs */
@Controller
public class PlaylistController {

	@Autowired
	PlaylistManager playlistManager;

	@Autowired
	YoutubeManager youtubeManager;

	@PostMapping(value = "/music/playlist/enquene", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> enquene(String path) throws MalformedURLException {
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

	@PostMapping(value = "/music/playlist/loop_all", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setLoopAll(boolean value) {
		playlistManager.setLoopAll(value);
		return new ResponseEntity<String>("set loop-all to " + value, HttpStatus.OK);
	}

	@PostMapping(value = "/music/playlist/loop_one", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setLoopOne(boolean value) {
		playlistManager.setLoopOne(value);
		return new ResponseEntity<String>("set loop-one to " + value, HttpStatus.OK);
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

	@GetMapping(value = "/music/playlist")
	public ResponseEntity<List<TrackMetadata>> getPlaylist() {
		return new ResponseEntity<List<TrackMetadata>>(playlistManager.getPlaylist(), HttpStatus.OK);
	}

}
