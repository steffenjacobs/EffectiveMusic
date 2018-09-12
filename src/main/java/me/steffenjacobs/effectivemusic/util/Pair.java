package me.steffenjacobs.effectivemusic.util;

/** @author Steffen Jacobs */
public class Pair<T1, T2> {
	final T1 x;
	final T2 y;

	public T1 getX() {
		return x;
	}

	public T2 getY() {
		return y;
	}

	public Pair(T1 x, T2 y) {
		super();
		this.x = x;
		this.y = y;
	}

}
