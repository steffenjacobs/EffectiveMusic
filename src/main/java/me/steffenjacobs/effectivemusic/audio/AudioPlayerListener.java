package me.steffenjacobs.effectivemusic.audio;

/** @author Steffen Jacobs */
public interface AudioPlayerListener {

	default void onStart() {
	};

	default void onStop() {
	};

	default void onPause() {
	};

	default void onResume() {
	};

	default void onFinish() {
	};

}
