package me.steffenjacobs.effectivemusic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.AudioPlayer.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;

/** @author Steffen Jacobs */
@Controller
public class MusicController {

	@Autowired
	AudioPlayer audioPlayer;
	
	@Autowired
	AudioEffectManager audioEffectManager;

	@PostMapping(value = "/music/play", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> playSong(String path) throws BasicPlayerException {
		audioPlayer.playAudio(path);
		return new ResponseEntity<String>("Playing file " + path, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/music/stop")
	public ResponseEntity<String> stop() throws BasicPlayerException {
		audioPlayer.stop();
		return new ResponseEntity<String>("Stopped music", HttpStatus.OK);
	}

	@PostMapping(value = "/music/pause")
	public ResponseEntity<String> pauseSong() throws BasicPlayerException {
		audioPlayer.pause();
		return new ResponseEntity<String>("Paused", HttpStatus.OK);
	}

	@PostMapping(value = "/music/resume")
	public ResponseEntity<String> resumeSong() throws BasicPlayerException {
		audioPlayer.resume();
		return new ResponseEntity<String>("Resumed", HttpStatus.OK);
	}

	@GetMapping(value = "/music/status")
	public ResponseEntity<String> getStatus() {
		Status status = audioPlayer.getStatus();
		return new ResponseEntity<String>("status: " + status, HttpStatus.OK);
	}

	@GetMapping(value = "/music/gain")
	public ResponseEntity<String> getGain() {
		double gain = audioPlayer.getGain();
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@PostMapping(value = "/music/gain", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setGain(double gain) throws BasicPlayerException {
		audioPlayer.setGain(gain);
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@GetMapping(value = "/music/position")
	public ResponseEntity<String> getPosition() throws BasicPlayerException {
		long position = audioPlayer.getMicrosecondPosition();
		return new ResponseEntity<String>("position: " + position / 1000000 + "s", HttpStatus.OK);
	}

	@PostMapping(value = "/music/position", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setPosition(long position) throws BasicPlayerException {
		audioPlayer.setPosition(position);
		return new ResponseEntity<String>("position: " + position, HttpStatus.OK);
	}

	@GetMapping(value = "/music/info")
	public ResponseEntity<TrackDTO> getTrackInfo() throws BasicPlayerException {
		return new ResponseEntity<TrackDTO>(audioPlayer.getTrackInformation(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/music/fadeTo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> fadeTo(double gain, long millis) throws BasicPlayerException {
		audioEffectManager.fadeTo(gain, millis);
		return new ResponseEntity<String>("fading...", HttpStatus.OK);
	}

}
