package me.steffenjacobs.effectivemusic.audio.dual;

import org.springframework.context.annotation.Bean;

import me.steffenjacobs.effectivemusic.audio.AudioPlayer;
import me.steffenjacobs.effectivemusic.audio.VLCMediaPlayerAdapter;

/** @author Steffen Jacobs */
// @Configuration
// TODO
public class DualPlayerConfiguration {
	@Bean(name = "player1")
	public AudioPlayer player1() {
		return new VLCMediaPlayerAdapter();
	}

	@Bean(name = "player2")
	public AudioPlayer player2() {
		return new VLCMediaPlayerAdapter();
	}
}
