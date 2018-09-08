package me.steffenjacobs.effectivemusic.audio;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.steffenjacobs.effectivemusic.util.AtomicDouble;

/** @author Steffen Jacobs */
public class Fade {
	private static final Logger LOG = LoggerFactory.getLogger(Fade.class);
	static final long TIMER_INTERVAL_MILLIS = 20;
	private static final double UPPER_LIMIT = 200;

	private AtomicDouble steps = new AtomicDouble();
	private AtomicDouble initialGain = new AtomicDouble(), targetGain = new AtomicDouble();
	private AtomicBoolean fadeUp = new AtomicBoolean(false);
	private AudioPlayer player;
	private AtomicBoolean paused = new AtomicBoolean(false);
	private final List<FadeCompleteListener> listeners = new CopyOnWriteArrayList<>();

	public Fade(FadeCompleteListener listener) {
		this.listeners.add(listener);
	}

	public void pauseFade() {
		paused.set(true);
	}

	public void resumeFade() {
		LOG.info("Fading from {} to {}", initialGain.get(), targetGain.get());
		paused.set(false);
	}

	public void setInitalGain(double newInitialGain) {
		this.initialGain.set(newInitialGain);
	}

	public void setTargetGain(double newTargetGain) {
		this.targetGain.set(newTargetGain);
	}

	public void setAudioPlayer(AudioPlayer newPlayer) {
		player = newPlayer;
	}

	public void setSteps(double newSteps) {
		steps.set(newSteps);
	}

	public void setFadeUp(boolean newFadeUp) {
		fadeUp.set(newFadeUp);
	}

	public void addListener(FadeCompleteListener listener) {
		listeners.add(listener);
	}

	public void runAsync() {
		LOG.info("Fading from {} to {}", initialGain.get(), targetGain.get());
		paused.set(false);
		final Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			double currentGain = initialGain.get();

			@Override
			public void run() {
				if (paused.get()) {
					return;
				}
				currentGain += (targetGain.get() - initialGain.get()) / steps.get();
				player.setGain(currentGain < 0 ? 0 : currentGain > UPPER_LIMIT ? UPPER_LIMIT : currentGain);

				if ((fadeUp.get() && currentGain >= targetGain.get()) || (!fadeUp.get() && currentGain <= targetGain.get())) {
					t.cancel();
					player.setGain(targetGain.get());

					listeners.forEach(FadeCompleteListener::onFadeComplete);
					LOG.info("Fade complete");
				}
			}
		}, 0, TIMER_INTERVAL_MILLIS);
	}

}
