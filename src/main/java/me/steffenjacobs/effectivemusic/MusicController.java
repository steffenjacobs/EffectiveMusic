package me.steffenjacobs.effectivemusic;

import java.net.MalformedURLException;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.JavazoomAudioPlayer.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;

/** @author Steffen Jacobs */
@Controller
public class MusicController {

	@Autowired
	JavazoomAudioPlayer javazoomAudioPlayer;

	@Autowired
	AudioEffectManager audioEffectManager;

	@Autowired
	YoutubeManager youtubeManager;

	@PostMapping(value = "/music/play", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> playSong(String path) throws MalformedURLException, BasicPlayerException {
		if (path.startsWith("https://www.youtube.com/watch?v=")) {
			javazoomAudioPlayer.stop();
			youtubeManager.playYoutube(path);
		} else {
			throw new NotImplementedException("Only youtube playback allowed atm");
			// TODO: Spotify
		}
		return new ResponseEntity<String>("Playing file " + path, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/music/stop")
	public ResponseEntity<String> stop() throws BasicPlayerException {
		javazoomAudioPlayer.stop();
		return new ResponseEntity<String>("Stopped music", HttpStatus.OK);
	}

	@PostMapping(value = "/music/pause")
	public ResponseEntity<String> pauseSong() throws BasicPlayerException {
		javazoomAudioPlayer.pause();
		return new ResponseEntity<String>("Paused", HttpStatus.OK);
	}

	@PostMapping(value = "/music/resume")
	public ResponseEntity<String> resumeSong() throws BasicPlayerException {
		javazoomAudioPlayer.resume();
		return new ResponseEntity<String>("Resumed", HttpStatus.OK);
	}

	@GetMapping(value = "/music/status")
	public ResponseEntity<String> getStatus() {
		Status status = javazoomAudioPlayer.getStatus();
		return new ResponseEntity<String>("status: " + status, HttpStatus.OK);
	}

	@GetMapping(value = "/music/gain")
	public ResponseEntity<String> getGain() {
		double gain = javazoomAudioPlayer.getGain();
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@PostMapping(value = "/music/gain", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setGain(double gain) throws BasicPlayerException {
		javazoomAudioPlayer.setGain(gain);
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@GetMapping(value = "/music/position")
	public ResponseEntity<String> getPosition() throws BasicPlayerException {
		long position = javazoomAudioPlayer.getMicrosecondPosition();
		return new ResponseEntity<String>("position: " + position / 1000000 + "s", HttpStatus.OK);
	}

	@PostMapping(value = "/music/position", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setPosition(long position) throws BasicPlayerException {
		javazoomAudioPlayer.setPosition(position);
		return new ResponseEntity<String>("position: " + position, HttpStatus.OK);
	}

	@GetMapping(value = "/music/info")
	public ResponseEntity<TrackDTO> getTrackInfo() throws BasicPlayerException {
		return new ResponseEntity<TrackDTO>(javazoomAudioPlayer.getTrackInformation(), HttpStatus.OK);
	}

	@PostMapping(value = "/music/fadeTo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> fadeTo(double gain, long millis) throws BasicPlayerException {
		audioEffectManager.fadeTo(gain, millis);
		return new ResponseEntity<String>("fading...", HttpStatus.OK);
	}

}
