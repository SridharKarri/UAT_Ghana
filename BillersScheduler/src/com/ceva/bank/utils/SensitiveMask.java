package com.ceva.bank.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class SensitiveMask extends PatternLayout {
	private static final String ACTMASK = "$1<HIDDEN>$2";
	private static final Pattern ACTPATTERN = Pattern
			.compile("([0-9]{3})[0-9]{0,4}([0-9]{3})");

	@Override
	public String format(LoggingEvent event) {
		if (event.getMessage() instanceof String) {
			String message = event.getRenderedMessage();

			Matcher matcher = ACTPATTERN.matcher(message);

			if (matcher.find()) {
				String maskedMessage = matcher.replaceAll(ACTMASK);

				Throwable throwable = event.getThrowableInformation() != null ? event
						.getThrowableInformation().getThrowable() : null;

				LoggingEvent maskedEvent = new LoggingEvent(
						event.fqnOfCategoryClass, Logger.getLogger(event
								.getLoggerName()), event.timeStamp,
						event.getLevel(), maskedMessage, throwable);

				return super.format(maskedEvent);
			}
		}

		return super.format(event);

	}
}
