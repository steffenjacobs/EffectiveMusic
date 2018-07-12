package me.steffenjacobs.effectivemusic;

import java.net.MalformedURLException;

import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.audio.AudioEffectManager;
import me.steffenjacobs.effectivemusic.audio.AudioPlayer;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.youtube.YoutubeManager;

/** @author Steffen Jacobs */
@Controller
public class MusicController {

	@Autowired
	AudioPlayer vlcPlayer;

	@Autowired
	AudioEffectManager audioEffectManager;

	@Autowired
	YoutubeManager youtubeManager;
	
	@Autowired
	PlaylistManager playlistManager;

	@PostMapping(value = "/music/play", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> playSong(String path) throws MalformedURLException, BasicPlayerException {
		playlistManager.clearPlaylist();
		if (path.startsWith("https://www.youtube.com/watch?v=")) {
			playlistManager.queue(new TrackMetadata(youtubeManager.getPlaybackUrl(path).toString()));
		} else {
			playlistManager.queue(new TrackMetadata(path));
		}
		return new ResponseEntity<String>("Playing file " + path, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/music/stop")
	public ResponseEntity<String> stop() throws BasicPlayerException {
		vlcPlayer.stop();
		return new ResponseEntity<String>("Stopped music", HttpStatus.OK);
	}

	@PostMapping(value = "/music/pause")
	public ResponseEntity<String> pauseSong() throws BasicPlayerException {
		vlcPlayer.pause();
		return new ResponseEntity<String>("Paused", HttpStatus.OK);
	}

	@PostMapping(value = "/music/resume")
	public ResponseEntity<String> resumeSong() throws BasicPlayerException {
		vlcPlayer.resume();
		return new ResponseEntity<String>("Resumed", HttpStatus.OK);
	}

	@GetMapping(value = "/music/status")
	public ResponseEntity<String> getStatus() {
		Status status = vlcPlayer.getStatus();
		return new ResponseEntity<String>("status: " + status, HttpStatus.OK);
	}

	@GetMapping(value = "/music/gain")
	public ResponseEntity<String> getGain() {
		double gain = vlcPlayer.getGain();
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@PostMapping(value = "/music/gain", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setGain(double gain) throws BasicPlayerException {
		vlcPlayer.setGain(gain);
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@GetMapping(value = "/music/position")
	public ResponseEntity<String> getPosition() throws BasicPlayerException {
		float position = vlcPlayer.getPosition();
		return new ResponseEntity<String>("position: " + position + "%", HttpStatus.OK);
	}

	@PostMapping(value = "/music/position", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setPosition(float position) throws BasicPlayerException {
		vlcPlayer.setPosition(position);
		return new ResponseEntity<String>("position: " + position, HttpStatus.OK);
	}

	@GetMapping(value = "/music/info")
	public ResponseEntity<TrackDTO> getTrackInfo() throws TagException {
		return new ResponseEntity<TrackDTO>(vlcPlayer.getTrackInformation(), HttpStatus.OK);
	}

	@PostMapping(value = "/music/fadeTo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> fadeTo(double gain, long millis) throws BasicPlayerException {
		audioEffectManager.fadeTo(gain, millis, vlcPlayer);
		return new ResponseEntity<String>("fading...", HttpStatus.OK);
	}

	@GetMapping(value = "/music/length")
	public ResponseEntity<String> getLength() {
		long length = vlcPlayer.getLength();
		return new ResponseEntity<String>("length: " + length + "ms", HttpStatus.OK);
	}

}
