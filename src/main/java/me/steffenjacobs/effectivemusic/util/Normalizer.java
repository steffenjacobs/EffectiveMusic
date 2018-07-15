package me.steffenjacobs.effectivemusic.util;

/** @author Steffen Jacobs */
public class Normalizer {

	public static class Interval {
		private final double start, end;

		public Interval(double start, double end) {
			super();
			this.start = start;
			this.end = end;
		}

		public double getStart() {
			return start;
		}

		public double getEnd() {
			return end;
		}
	}

	public static double mapZeroToOneToZeroToOnehundred(double input) {
		return mapRelativeValue(input, new Interval(0, 1), new Interval(0, 100));
	}

	public static double mapZeroToOnehundredToZeroToOne(double input) {
		return mapRelativeValue(input, new Interval(0, 100), new Interval(0, 1));
	}

	public static double mapToZeroToOne(double input, Interval source) {
		return mapRelativeValue(input, source, new Interval(0, 1));
	}

	public static double mapZeroToOneToX(double input, Interval target) {
		return mapRelativeValue(input, new Interval(0, 1), target);
	}

	public static double mapRelativeValue(double input, Interval source, Interval target) {
		source = orderInterval(source);
		target = orderInterval(target);
		double sourceSize = source.getEnd() - source.getStart();
		double targetSize = target.getEnd() - target.getStart();

		return target.getStart() + ((input - source.getStart()) / sourceSize) * targetSize;
	}

	private static Interval orderInterval(Interval interval) {
		if (interval.getStart() <= interval.getEnd()) {
			return interval;
		}
		return new Interval(interval.getEnd(), interval.getStart());
	}
}
