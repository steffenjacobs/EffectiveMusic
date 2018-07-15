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

import me.steffenjacobs.effectivemusic.audio.AudioEffectManager;
import me.steffenjacobs.effectivemusic.audio.AudioPlayerManager;
import me.steffenjacobs.effectivemusic.domain.LiveTrackDTO;
import me.steffenjacobs.effectivemusic.domain.Status;
import me.steffenjacobs.effectivemusic.domain.TrackDTO;
import me.steffenjacobs.effectivemusic.domain.TrackMetadata;
import me.steffenjacobs.effectivemusic.util.Base64Service;
import me.steffenjacobs.effectivemusic.youtube.YoutubeManager;

/** @author Steffen Jacobs */
@Controller
public class MusicController {

	@Autowired
	AudioPlayerManager audioPlayerManager;

	@Autowired
	AudioEffectManager audioEffectManager;

	@Autowired
	YoutubeManager youtubeManager;

	@Autowired
	PlaylistManager playlistManager;

	@Autowired
	Base64Service base64Service;

	@PostMapping(value = "/music/play", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> playSong(String path) throws MalformedURLException {
		if (base64Service.isBase64(path)) {
			path = base64Service.decode(path);
		}
		playlistManager.clearPlaylist();
		if (path.startsWith("https://www.youtube.com/watch?v=")) {
			playlistManager.queue(youtubeManager.getPlaybackUrl(path));
		} else {
			playlistManager.queue(new TrackMetadata(path));
		}
		return new ResponseEntity<String>("Playing file " + path, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/music/stop")
	public ResponseEntity<String> stop() {
		audioPlayerManager.stop();
		return new ResponseEntity<String>("Stopped music", HttpStatus.OK);
	}

	@PostMapping(value = "/music/pause")
	public ResponseEntity<String> pauseSong() {
		audioPlayerManager.pause();
		return new ResponseEntity<String>("Paused", HttpStatus.OK);
	}

	@PostMapping(value = "/music/resume")
	public ResponseEntity<String> resumeSong() {
		audioPlayerManager.resume();
		return new ResponseEntity<String>("Resumed", HttpStatus.OK);
	}

	@GetMapping(value = "/music/status")
	public ResponseEntity<String> getStatus() {
		Status status = audioPlayerManager.getStatus();
		return new ResponseEntity<String>("status: " + status, HttpStatus.OK);
	}

	@GetMapping(value = "/music/gain")
	public ResponseEntity<String> getGain() {
		double gain = audioPlayerManager.getGain();
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@PostMapping(value = "/music/gain", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setGain(double gain) {
		audioPlayerManager.setGain(gain);
		return new ResponseEntity<String>("gain: " + gain, HttpStatus.OK);
	}

	@GetMapping(value = "/music/position")
	public ResponseEntity<String> getPosition() {
		float position = audioPlayerManager.getPosition();
		return new ResponseEntity<String>("position: " + position + "%", HttpStatus.OK);
	}

	@PostMapping(value = "/music/position", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> setPosition(float position) {
		audioPlayerManager.setPosition(position);
		return new ResponseEntity<String>("position: " + position, HttpStatus.OK);
	}

	@GetMapping(value = "/music/info")
	public ResponseEntity<TrackDTO> getTrackInfo() {
		return new ResponseEntity<TrackDTO>(audioPlayerManager.getTrackInformation(), HttpStatus.OK);
	}

	@GetMapping(value = "/music/live_info")
	public ResponseEntity<LiveTrackDTO> getLiveTrackInfo() {
		return new ResponseEntity<LiveTrackDTO>(audioPlayerManager.getLiveTrackInformation(), HttpStatus.OK);
	}

	@PostMapping(value = "/music/fadeTo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> fadeTo(double gain, long millis) {
		audioEffectManager.fadeTo(gain, millis, audioPlayerManager.getCurrentAudioPlayer());
		return new ResponseEntity<String>("fading...", HttpStatus.OK);
	}

	@GetMapping(value = "/music/length")
	public ResponseEntity<String> getLength() throws TagException {
		long length = audioPlayerManager.getTrackInformation().getLength();
		return new ResponseEntity<String>("length: " + length + "ms", HttpStatus.OK);
	}

}
