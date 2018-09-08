package me.steffenjacobs.effectivemusic.util;

/** @author Steffen Jacobs */
public class Wrapper<T> {

	private T t;

	public T getValue() {
		return t;
	}

	public void setValue(T t) {
		this.t = t;
	}

}
