package me.steffenjacobs.effectivemusic.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import me.steffenjacobs.effectivemusic.util.Normalizer.Interval;

/** @author Steffen Jacobs */
public class NormalizerTest {

	@Test
	public void testSmallToBig() {
		double result = Normalizer.mapRelativeValue(0.5, new Interval(0, 1), new Interval(0, 100));
		assertEquals(50, result, 0.01);
	}

	@Test
	public void bigToSmall() {
		double result = Normalizer.mapRelativeValue(50, new Interval(0, 100), new Interval(0, 1));
		assertEquals(0.5, result, 0.01);
	}

	@Test
	public void testWithSourceIntervalStartBiggerThanZero() {
		double result = Normalizer.mapRelativeValue(15.5, new Interval(10, 20), new Interval(0, 1));
		assertEquals(0.55, result, 0.01);
	}

	@Test
	public void testWithTargetIntervalStartBiggerThanZero() {
		double result = Normalizer.mapRelativeValue(0.55, new Interval(0, 1), new Interval(10, 20));
		assertEquals(15.5, result, 0.01);
	}

	@Test
	public void testWithBothIntervalsStartBiggerThanZero() {
		double result = Normalizer.mapRelativeValue(75, new Interval(50, 100), new Interval(10, 20));
		assertEquals(15, result, 0.01);
	}

	@Test
	public void testSourceIntervalBackwards() {
		double result = Normalizer.mapRelativeValue(0.5, new Interval(1, 0), new Interval(0, 100));
		assertEquals(50, result, 0.01);
	}

	@Test
	public void testTargetIntervalBackwards() {
		double result = Normalizer.mapRelativeValue(50, new Interval(0, 100), new Interval(1, 0));
		assertEquals(0.5, result, 0.01);
	}

	@Test
	public void testMapZeroToOnehundredToZeroToOne() {
		double result = Normalizer.mapZeroToOnehundredToZeroToOne(50);
		assertEquals(0.5, result, 0.01);
	}

	@Test
	public void testMapZeroToOneToZeroToOnehundred() {
		double result = Normalizer.mapZeroToOneToZeroToOnehundred(0.5);
		assertEquals(50, result, 0.01);
	}

	@Test
	public void testMapToZeroToOne() {
		double result = Normalizer.mapToZeroToOne(50, new Interval(0, 100));
		assertEquals(0.5, result, 0.01);
	}

	@Test
	public void testMapZeroToOneToX() {
		double result = Normalizer.mapZeroToOneToX(.5, new Interval(0, 100));
		assertEquals(50, result, 0.01);
	}
}
