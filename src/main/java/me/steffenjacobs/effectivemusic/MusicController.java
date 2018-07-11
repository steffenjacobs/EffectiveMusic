package me.steffenjacobs.effectivemusic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.AudioPlayer.Status;

/** @author Steffen Jacobs */
@Controller
public class MusicController {

	@Autowired
	AudioPlayer audioPlayer;

	@PostMapping(value = "/music/play", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> playSong(String path) throws BasicPlayerException {
		audioPlayer.playAudio(path);
		return new ResponseEntity<String>("Playing file " + path, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/music/stop", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> stop() throws BasicPlayerException {
		audioPlayer.stop();
		return new ResponseEntity<String>("Stopped music", HttpStatus.OK);
	}

	@PostMapping(value = "/music/pause", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> pauseSong() throws BasicPlayerException {
		audioPlayer.pause();
		return new ResponseEntity<String>("Paused", HttpStatus.OK);
	}

	@PostMapping(value = "/music/resume", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> resumeSong() throws BasicPlayerException {
		audioPlayer.resume();
		return new ResponseEntity<String>("Resumed", HttpStatus.OK);
	}

	@PostMapping(value = "/music/status", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> getStatus(String path) {
		Status status = audioPlayer.getStatus();
		return new ResponseEntity<String>("status: " + status, HttpStatus.OK);
	}

	@GetMapping(value = "/music/gain", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> getGain() {
		double gain = audioPlayer.getGain();
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@PostMapping(value = "/music/gain", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setGain(double gain) throws BasicPlayerException {
		audioPlayer.setGain(gain);
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

}
