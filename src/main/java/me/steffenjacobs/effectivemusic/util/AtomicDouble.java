package me.steffenjacobs.effectivemusic.util;

import java.util.concurrent.atomic.AtomicLong;

/** @author Steffen Jacobs */
public class AtomicDouble extends Number {
	private static final long serialVersionUID = -8657517521803763885L;
	private AtomicLong bits;

	public AtomicDouble() {
		this(0d);
	}

	public AtomicDouble(double initialValue) {
		bits = new AtomicLong(Double.doubleToLongBits(initialValue));
	}

	public final boolean compareAndSet(double expect, double update) {
		return bits.compareAndSet(Double.doubleToLongBits(expect), Double.doubleToLongBits(update));
	}

	public final void set(double newValue) {
		bits.set(Double.doubleToLongBits(newValue));
	}

	public final double get() {
		return Double.longBitsToDouble(bits.get());
	}

	public final double getAndSet(float newValue) {
		return Double.longBitsToDouble(bits.getAndSet(Double.doubleToLongBits(newValue)));
	}

	public final boolean weakCompareAndSet(double expect, double update) {
		return bits.weakCompareAndSet(Double.doubleToLongBits(expect), Double.doubleToLongBits(update));
	}

	@Override
	public double doubleValue() {
		return get();
	}

	@Override
	public int intValue() {
		return (int) get();
	}

	@Override
	public long longValue() {
		return (long) get();
	}

	@Override
	public float floatValue() {
		return (float) get();
	}
}
