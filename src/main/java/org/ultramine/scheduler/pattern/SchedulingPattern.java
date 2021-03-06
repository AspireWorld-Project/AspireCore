package org.ultramine.scheduler.pattern;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * <p>
 * A UNIX crontab-like pattern is a string split in five space separated parts.
 * Each part is intented as:
 * </p>
 * <ol>
 * <li><strong>Minutes sub-pattern</strong>. During which minutes of the hour
 * should the task been launched? The values range is from 0 to 59.</li>
 * <li><strong>Hours sub-pattern</strong>. During which hours of the day should
 * the task been launched? The values range is from 0 to 23.</li>
 * <li><strong>Days of month sub-pattern</strong>. During which days of the
 * month should the task been launched? The values range is from 1 to 31. The
 * special value L can be used to recognize the last day of month.</li>
 * <li><strong>Months sub-pattern</strong>. During which months of the year
 * should the task been launched? The values range is from 1 (January) to 12
 * (December), otherwise this sub-pattern allows the aliases &quot;jan&quot;,
 * &quot;feb&quot;, &quot;mar&quot;, &quot;apr&quot;, &quot;may&quot;,
 * &quot;jun&quot;, &quot;jul&quot;, &quot;aug&quot;, &quot;sep&quot;,
 * &quot;oct&quot;, &quot;nov&quot; and &quot;dec&quot;.</li>
 * <li><strong>Days of week sub-pattern</strong>. During which days of the week
 * should the task been launched? The values range is from 0 (Sunday) to 6
 * (Saturday), otherwise this sub-pattern allows the aliases &quot;sun&quot;,
 * &quot;mon&quot;, &quot;tue&quot;, &quot;wed&quot;, &quot;thu&quot;,
 * &quot;fri&quot; and &quot;sat&quot;.</li>
 * </ol>
 * <p>
 * The star wildcard character is also admitted, indicating &quot;every minute
 * of the hour&quot;, &quot;every hour of the day&quot;, &quot;every day of the
 * month&quot;, &quot;every month of the year&quot; and &quot;every day of the
 * week&quot;, according to the sub-pattern in which it is used.
 * </p>
 * <p>
 * Once the scheduler is started, a task will be launched when the five parts in
 * its scheduling pattern will be true at the same time.
 * </p>
 * <p>
 * Some examples:
 * </p>
 * <p>
 * <strong>5 * * * *</strong><br />
 * This pattern causes a task to be launched once every hour, at the begin of
 * the fifth minute (00:05, 01:05, 02:05 etc.).
 * </p>
 * <p>
 * <strong>* * * * *</strong><br />
 * This pattern causes a task to be launched every minute.
 * </p>
 * <p>
 * <strong>* 12 * * Mon</strong><br />
 * This pattern causes a task to be launched every minute during the 12th hour
 * of Monday.
 * </p>
 * <p>
 * <strong>* 12 16 * Mon</strong><br />
 * This pattern causes a task to be launched every minute during the 12th hour
 * of Monday, 16th, but only if the day is the 16th of the month.
 * </p>
 * <p>
 * Every sub-pattern can contain two or more comma separated values.
 * </p>
 * <p>
 * <strong>59 11 * * 1,2,3,4,5</strong><br />
 * This pattern causes a task to be launched at 11:59AM on Monday, Tuesday,
 * Wednesday, Thursday and Friday.
 * </p>
 * <p>
 * Values intervals are admitted and defined using the minus character.
 * </p>
 * <p>
 * <strong>59 11 * * 1-5</strong><br />
 * This pattern is equivalent to the previous one.
 * </p>
 * <p>
 * The slash character can be used to identify step values within a range. It
 * can be used both in the form <em>*&#47;c</em> and <em>a-b/c</em>. The
 * subpattern is matched every <em>c</em> values of the range
 * <em>0,maxvalue</em> or <em>a-b</em>.
 * </p>
 * <p>
 * <strong>*&#47;5 * * * *</strong><br />
 * This pattern causes a task to be launched every 5 minutes (0:00, 0:05, 0:10,
 * 0:15 and so on).
 * </p>
 * <p>
 * <strong>3-18&#47;5 * * * *</strong><br />
 * This pattern causes a task to be launched every 5 minutes starting from the
 * third minute of the hour, up to the 18th (0:03, 0:08, 0:13, 0:18, 1:03, 1:08
 * and so on).
 * </p>
 * <p>
 * <strong>*&#47;15 9-17 * * *</strong><br />
 * This pattern causes a task to be launched every 15 minutes between the 9th
 * and 17th hour of the day (9:00, 9:15, 9:30, 9:45 and so on... note that the
 * last execution will be at 17:45).
 * </p>
 * <p>
 * All the fresh described syntax rules can be used together.
 * </p>
 * <p>
 * <strong>* 12 10-16&#47;2 * *</strong><br />
 * This pattern causes a task to be launched every minute during the 12th hour
 * of the day, but only if the day is the 10th, the 12th, the 14th or the 16th
 * of the month.
 * </p>
 * <p>
 * <strong>* 12 1-15,17,20-25 * *</strong><br />
 * This pattern causes a task to be launched every minute during the 12th hour
 * of the day, but the day of the month must be between the 1st and the 15th,
 * the 20th and the 25, or at least it must be the 17th.
 * </p>
 * <p>
 * Finally cron4j lets you combine more scheduling patterns into one, with the
 * pipe character:
 * </p>
 * <p>
 * <strong>0 5 * * *|8 10 * * *|22 17 * * *</strong><br />
 * This pattern causes a task to be launched every day at 05:00, 10:08 and
 * 17:22.
 * </p>
 */
public class SchedulingPattern {
	private static final IValueParser MINUTE_VALUE_PARSER = new MinuteValueParser();
	private static final IValueParser HOUR_VALUE_PARSER = new HourValueParser();
	private static final IValueParser DAY_OF_MONTH_VALUE_PARSER = new DayOfMonthValueParser();
	private static final IValueParser MONTH_VALUE_PARSER = new MonthValueParser();
	private static final IValueParser DAY_OF_WEEK_VALUE_PARSER = new DayOfWeekValueParser();

	/** The pattern as a string. */
	private final String asString;

	protected ArrayList<ValueMatcherCollection> valueMatchers = new ArrayList<>();

	/**
	 * Builds a SchedulingPattern parsing it from a string.
	 *
	 * @param pattern
	 *            The pattern as a crontab-like string.
	 * @throws InvalidPatternException
	 *             If the supplied string is not a valid pattern.
	 */
	public SchedulingPattern(String pattern) throws InvalidPatternException {
		asString = pattern;
		StringTokenizer st1 = new StringTokenizer(pattern, "|");
		if (st1.countTokens() < 1)
			throw new InvalidPatternException("invalid pattern: \"" + pattern + "\"");
		while (st1.hasMoreTokens()) {
			String localPattern = st1.nextToken();
			StringTokenizer st2 = new StringTokenizer(localPattern, " \t");
			if (st2.countTokens() != 5)
				throw new InvalidPatternException("invalid pattern: \"" + localPattern + "\"");
			ValueMatcherCollection matcher = new ValueMatcherCollection();
			try {
				matcher.minute = buildValueMatcher(st2.nextToken(), MINUTE_VALUE_PARSER);
			} catch (Exception e) {
				throw new InvalidPatternException("invalid pattern \"" + localPattern
						+ "\". Error parsing minutes field: " + e.getMessage() + ".");
			}
			try {
				matcher.hour = buildValueMatcher(st2.nextToken(), HOUR_VALUE_PARSER);
			} catch (Exception e) {
				throw new InvalidPatternException(
						"invalid pattern \"" + localPattern + "\". Error parsing hours field: " + e.getMessage() + ".");
			}
			try {
				matcher.dayOfMonth = buildValueMatcher(st2.nextToken(), DAY_OF_MONTH_VALUE_PARSER);
			} catch (Exception e) {
				throw new InvalidPatternException("invalid pattern \"" + localPattern
						+ "\". Error parsing days of month field: " + e.getMessage() + ".");
			}
			try {
				matcher.month = buildValueMatcher(st2.nextToken(), MONTH_VALUE_PARSER);
			} catch (Exception e) {
				throw new InvalidPatternException("invalid pattern \"" + localPattern
						+ "\". Error parsing months field: " + e.getMessage() + ".");
			}
			try {
				matcher.dayOfWeek = buildValueMatcher(st2.nextToken(), DAY_OF_WEEK_VALUE_PARSER);
			} catch (Exception e) {
				throw new InvalidPatternException("invalid pattern \"" + localPattern
						+ "\". Error parsing days of week field: " + e.getMessage() + ".");
			}
			valueMatchers.add(matcher);
		}
	}

	/**
	 * A ValueMatcher utility builder.
	 *
	 * @param str
	 *            The pattern part for the ValueMatcher creation.
	 * @param parser
	 *            The parser used to parse the values.
	 * @return The requested ValueMatcher.
	 * @throws Exception
	 *             If the supplied pattern part is not valid.
	 */
	private IValueMatcher buildValueMatcher(String str, IValueParser parser) throws Exception {
		if (str.length() == 1 && str.equals("*"))
			return new AlwaysTrueValueMatcher();
		TIntSet values = new TIntHashSet();
		String[] parts = StringUtils.split(str, ',');
		for (int i = 0; i < parts.length; i++) {
			String element = parts[i];
			TIntList local;
			try {
				local = parseListElement(element, parser);
			} catch (Exception e) {
				throw new Exception(
						"invalid field \"" + str + "\", invalid element \"" + element + "\", " + e.getMessage());
			}
			values.addAll(local);
		}
		if (values.size() == 0)
			throw new Exception("invalid field \"" + str + "\"");
		if (parser == DAY_OF_MONTH_VALUE_PARSER)
			return new DayOfMonthValueMatcher(values);
		else
			return new IntSetValueMatcher(values);
	}

	/**
	 * Parses an element of a list of values of the pattern.
	 *
	 * @param str
	 *            The element string.
	 * @param parser
	 *            The parser used to parse the values.
	 * @return A list of integers representing the allowed values.
	 * @throws Exception
	 *             If the supplied pattern part is not valid.
	 */
	private TIntList parseListElement(String str, IValueParser parser) throws Exception {
		String[] parts = StringUtils.split(str, '/');
		if (parts.length < 1 || parts.length > 2)
			throw new Exception("syntax error");
		TIntList values;
		try {
			values = parseRange(parts[0], parser);
		} catch (Exception e) {
			throw new Exception("invalid range, " + e.getMessage());
		}
		if (parts.length == 2) {
			String dStr = parts[1];
			int div;
			try {
				div = Integer.parseInt(dStr);
			} catch (NumberFormatException e) {
				throw new Exception("invalid divisor \"" + dStr + "\"");
			}
			if (div < 1)
				throw new Exception("non positive divisor \"" + div + "\"");
			TIntList values2 = new TIntArrayList(values.size() / div + 1);
			for (int i = 0; i < values.size(); i += div) {
				values2.add(values.get(i));
			}
			return values2;
		} else
			return values;
	}

	/**
	 * Parses a range of values.
	 *
	 * @param str
	 *            The range string.
	 * @param parser
	 *            The parser used to parse the values.
	 * @return A list of integers representing the allowed values.
	 * @throws Exception
	 *             If the supplied pattern part is not valid.
	 */
	private TIntList parseRange(String str, IValueParser parser) throws Exception {
		if (str.equals("*")) {
			int min = parser.getMinValue();
			int max = parser.getMaxValue();
			TIntList values = new TIntArrayList();
			for (int i = min; i <= max; i++) {
				values.add(i);
			}
			return values;
		}
		String[] parts = StringUtils.split(str, '-');
		if (parts.length < 1 || parts.length > 2)
			throw new Exception("syntax error");
		String v1Str = parts[0];
		int v1;
		try {
			v1 = parser.parse(v1Str);
		} catch (Exception e) {
			throw new Exception("invalid value \"" + v1Str + "\", " + e.getMessage());
		}
		if (parts.length == 1) {
			TIntList values = new TIntArrayList();
			values.add(v1);
			return values;
		} else {
			String v2Str = parts[1];
			int v2;
			try {
				v2 = parser.parse(v2Str);
			} catch (Exception e) {
				throw new Exception("invalid value \"" + v2Str + "\", " + e.getMessage());
			}
			TIntList values = new TIntArrayList();
			if (v1 < v2) {
				for (int i = v1; i <= v2; i++) {
					values.add(i);
				}
			} else if (v1 > v2) {
				int min = parser.getMinValue();
				int max = parser.getMaxValue();
				for (int i = v1; i <= max; i++) {
					values.add(i);
				}
				for (int i = min; i <= v2; i++) {
					values.add(i);
				}
			} else {
				// v1 == v2
				values.add(v1);
			}
			return values;
		}
	}

	/**
	 * This methods returns true if the given timestamp (expressed as a UNIX-era
	 * millis value) matches the pattern, according to the given time zone.
	 *
	 * @param timezone
	 *            A time zone.
	 * @param millis
	 *            The timestamp, as a UNIX-era millis value.
	 * @return true if the given timestamp matches the pattern.
	 */
	public boolean match(TimeZone timezone, long millis) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(millis);
		if (timezone != null) {
			gc.setTimeZone(timezone);
		}
		int minute = gc.get(Calendar.MINUTE);
		int hour = gc.get(Calendar.HOUR_OF_DAY);
		int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
		int month = gc.get(Calendar.MONTH) + 1;
		int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK) - 1;
		int year = gc.get(Calendar.YEAR);
		for (int i = 0; i < valueMatchers.size(); i++) {
			ValueMatcherCollection matcher = valueMatchers.get(i);
			boolean eval = matcher.minute.match(minute) && matcher.hour.match(hour)
					&& (matcher.dayOfMonth instanceof DayOfMonthValueMatcher
							? ((DayOfMonthValueMatcher) matcher.dayOfMonth).match(dayOfMonth, month,
									gc.isLeapYear(year))
							: matcher.dayOfMonth.match(dayOfMonth))
					&& matcher.month.match(month) && matcher.dayOfWeek.match(dayOfWeek);
			if (eval)
				return true;
		}
		return false;
	}

	/**
	 * This methods returns true if the given timestamp (expressed as a UNIX-era
	 * millis value) matches the pattern, according to the system default time zone.
	 *
	 * @param millis
	 *            The timestamp, as a UNIX-era millis value.
	 * @return true if the given timestamp matches the pattern.
	 */
	public boolean match(long millis) {
		return match(null, millis);
	}

	/**
	 * Returns the pattern as a string.
	 *
	 * @return The pattern as a string.
	 */
	@Override
	public String toString() {
		return asString;
	}

	/**
	 * This utility method changes an alias to an int value.
	 *
	 * @param value
	 *            The value.
	 * @param aliases
	 *            The aliases list.
	 * @param offset
	 *            The offset appplied to the aliases list indices.
	 * @return The parsed value.
	 * @throws Exception
	 *             If the expressed values doesn't match any alias.
	 */
	private static int parseAlias(String value, String[] aliases, int offset) throws Exception {
		for (int i = 0; i < aliases.length; i++) {
			if (aliases[i].equalsIgnoreCase(value))
				return offset + i;
		}
		throw new Exception("invalid alias \"" + value + "\"");
	}

	/**
	 * Definition for a value parser.
	 */
	private interface IValueParser {
		/**
		 * Attempts to parse a value.
		 *
		 * @param value
		 *            The value.
		 * @return The parsed value.
		 * @throws Exception
		 *             If the value can't be parsed.
		 */
		int parse(String value) throws Exception;

		/**
		 * Returns the minimum value accepred by the parser.
		 *
		 * @return The minimum value accepred by the parser.
		 */
		int getMinValue();

		/**
		 * Returns the maximum value accepred by the parser.
		 *
		 * @return The maximum value accepred by the parser.
		 */
		int getMaxValue();
	}

	/**
	 * A simple value parser.
	 */
	private static class SimpleValueParser implements IValueParser {
		/**
		 * The minimum allowed value.
		 */
		protected int minValue;

		/**
		 * The maximum allowed value.
		 */
		protected int maxValue;

		/**
		 * Builds the value parser.
		 *
		 * @param minValue
		 *            The minimum allowed value.
		 * @param maxValue
		 *            The maximum allowed value.
		 */
		public SimpleValueParser(int minValue, int maxValue) {
			this.minValue = minValue;
			this.maxValue = maxValue;
		}

		@Override
		public int parse(String value) throws Exception {
			int i;
			try {
				i = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				throw new Exception("invalid integer value");
			}
			if (i < minValue || i > maxValue)
				throw new Exception("value out of range");
			return i;
		}

		@Override
		public int getMinValue() {
			return minValue;
		}

		@Override
		public int getMaxValue() {
			return maxValue;
		}
	}

	/**
	 * The minutes value parser.
	 */
	private static class MinuteValueParser extends SimpleValueParser {
		/**
		 * Builds the value parser.
		 */
		public MinuteValueParser() {
			super(0, 59);
		}
	}

	/**
	 * The hours value parser.
	 */
	private static class HourValueParser extends SimpleValueParser {
		/**
		 * Builds the value parser.
		 */
		public HourValueParser() {
			super(0, 23);
		}
	}

	/**
	 * The days of month value parser.
	 */
	private static class DayOfMonthValueParser extends SimpleValueParser {
		/**
		 * Builds the value parser.
		 */
		public DayOfMonthValueParser() {
			super(1, 31);
		}

		/**
		 * Added to support last-day-of-month.
		 *
		 * @param value
		 *            The value to be parsed
		 * @return the integer day of the month or 32 for last day of the month
		 * @throws Exception
		 *             if the input value is invalid
		 */
		@Override
		public int parse(String value) throws Exception {
			if (value.equalsIgnoreCase("L"))
				return 32;
			else
				return super.parse(value);
		}
	}

	/**
	 * The value parser for the months field.
	 */
	private static class MonthValueParser extends SimpleValueParser {
		/**
		 * Months aliases.
		 */
		private static final String[] ALIASES = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov",
				"dec" };

		/**
		 * Builds the months value parser.
		 */
		public MonthValueParser() {
			super(1, 12);
		}

		@Override
		public int parse(String value) throws Exception {
			try {
				// try as a simple value
				return super.parse(value);
			} catch (Exception e) {
				// try as an alias
				return parseAlias(value, ALIASES, 1);
			}
		}
	}

	/**
	 * The value parser for the months field.
	 */
	private static class DayOfWeekValueParser extends SimpleValueParser {
		/**
		 * Days of week aliases.
		 */
		private static final String[] ALIASES = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };

		/**
		 * Builds the months value parser.
		 */
		public DayOfWeekValueParser() {
			super(0, 7);
		}

		@Override
		public int parse(String value) throws Exception {
			try {
				// try as a simple value
				return super.parse(value) % 7;
			} catch (Exception e) {
				// try as an alias
				return parseAlias(value, ALIASES, 0);
			}
		}
	}

	private static class ValueMatcherCollection {
		IValueMatcher minute;
		IValueMatcher hour;
		IValueMatcher dayOfMonth;
		IValueMatcher month;
		IValueMatcher dayOfWeek;
	}
}
