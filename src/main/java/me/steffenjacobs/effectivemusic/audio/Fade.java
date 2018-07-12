package me.steffenjacobs.effectivemusic.audio;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import me.steffenjacobs.effectivemusic.util.AtomicDouble;

/** @author Steffen Jacobs */
public class Fade {
	private static final Logger LOG = LoggerFactory.getLogger(AudioEffectManager.class);
	static final long TIMER_INTERVAL_MILLIS = 100;
	private static final double UPPER_LIMIT = 200;

	private AtomicDouble steps = new AtomicDouble();
	private AtomicDouble initialGain = new AtomicDouble(), targetGain = new AtomicDouble();
	private AtomicBoolean fadeUp = new AtomicBoolean(false);
	private AudioPlayer player;
	private AtomicBoolean paused = new AtomicBoolean(false);
	private final FadeCompleteListener listener;

	public Fade(FadeCompleteListener listener) {
		this.listener = listener;
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
				try {
					player.setGain(currentGain < 0 ? 0 : currentGain > UPPER_LIMIT ? UPPER_LIMIT : currentGain);

					if ((fadeUp.get() && currentGain >= targetGain.get()) || (!fadeUp.get() && currentGain <= targetGain.get())) {
						t.cancel();
						player.setGain(targetGain.get());

						if (listener != null) {
							listener.onFadeComplete();
						}
						LOG.info("Fade complete");
					}
				} catch (BasicPlayerException e) {
					e.printStackTrace();
				}

			}
		}, 0, TIMER_INTERVAL_MILLIS);
	}

}
