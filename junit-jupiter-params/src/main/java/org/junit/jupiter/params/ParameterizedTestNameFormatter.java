/*
 * Copyright 2015-2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.params;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_PLACEHOLDER;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_WITH_NAMES_PLACEHOLDER;
import static org.junit.jupiter.params.ParameterizedTest.DEFAULT_NAMED_ARGUMENTS_DISPLAY_NAME;
import static org.junit.jupiter.params.ParameterizedTest.DISPLAY_NAME_PLACEHOLDER;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;
import static org.junit.jupiter.params.ParameterizedTest.NAMED_ARGUMENTS_PLACEHOLDER;

import java.text.Format;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.Arguments.NamedArguments;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.util.StringUtils;

/**
 * @since 5.0
 */
class ParameterizedTestNameFormatter {

	private static final char ELLIPSIS = '\u2026';
	private static final String TEMPORARY_DISPLAY_NAME_PLACEHOLDER = "~~~JUNIT_DISPLAY_NAME~~~";

	private final boolean useDefaultDisplayName;
	private final String pattern;
	private final String displayName;
	private final ParameterizedTestMethodContext methodContext;
	private final int argumentMaxLength;

	ParameterizedTestNameFormatter(boolean useDefaultDisplayName, String pattern, String displayName,
			ParameterizedTestMethodContext methodContext, int argumentMaxLength) {

		this.useDefaultDisplayName = useDefaultDisplayName;
		this.pattern = pattern;
		this.displayName = displayName;
		this.methodContext = methodContext;
		this.argumentMaxLength = argumentMaxLength;
	}

	String format(int invocationIndex, Arguments arguments, Object[] consumedArguments) {
		try {
			return formatSafely(invocationIndex, arguments, consumedArguments);
		}
		catch (Exception ex) {
			String message = "The display name pattern defined for the parameterized test is invalid. "
					+ "See nested exception for further details.";
			throw new JUnitException(message, ex);
		}
	}

	private String formatSafely(int invocationIndex, Arguments arguments, Object[] consumedArguments) {
		String pattern = this.pattern;
		if (this.useDefaultDisplayName && arguments instanceof NamedArguments) {
			pattern = DEFAULT_NAMED_ARGUMENTS_DISPLAY_NAME;
		}
		Object[] namedArguments = extractNamedArguments(consumedArguments);
		pattern = prepareMessageFormatPattern(pattern, invocationIndex, arguments, namedArguments);
		MessageFormat format = new MessageFormat(pattern);
		Object[] humanReadableArguments = makeReadable(format, namedArguments);
		String formatted = format.format(humanReadableArguments);
		return formatted.replace(TEMPORARY_DISPLAY_NAME_PLACEHOLDER, this.displayName);
	}

	private Object[] extractNamedArguments(Object[] arguments) {
		return Arrays.stream(arguments) //
				.map(argument -> argument instanceof Named ? ((Named<?>) argument).getName() : argument) //
				.toArray();
	}

	private String prepareMessageFormatPattern(String pattern, int invocationIndex, Arguments arguments,
			Object[] argumentsArray) {

		String result = pattern//
				.replace(DISPLAY_NAME_PLACEHOLDER, TEMPORARY_DISPLAY_NAME_PLACEHOLDER)//
				.replace(INDEX_PLACEHOLDER, String.valueOf(invocationIndex));

		if (result.contains(NAMED_ARGUMENTS_PLACEHOLDER)) {
			if (!(arguments instanceof NamedArguments)) {
				throw new ExtensionConfigurationException(
					String.format("When the display name pattern for a @ParameterizedTest contains %s, "
							+ "the arguments must be supplied as NamedArguments.",
						NAMED_ARGUMENTS_PLACEHOLDER));
			}
			result = result.replace(NAMED_ARGUMENTS_PLACEHOLDER, ((NamedArguments) arguments).getName());
		}

		if (result.contains(ARGUMENTS_WITH_NAMES_PLACEHOLDER)) {
			result = result.replace(ARGUMENTS_WITH_NAMES_PLACEHOLDER, argumentsWithNamesPattern(argumentsArray));
		}

		if (result.contains(ARGUMENTS_PLACEHOLDER)) {
			result = result.replace(ARGUMENTS_PLACEHOLDER, argumentsPattern(argumentsArray));
		}

		return result;
	}

	private String argumentsWithNamesPattern(Object[] arguments) {
		return IntStream.range(0, arguments.length) //
				.mapToObj(index -> methodContext.getParameterName(index).map(name -> name + "=").orElse("") + "{"
						+ index + "}") //
				.collect(joining(", "));
	}

	private String argumentsPattern(Object[] arguments) {
		return IntStream.range(0, arguments.length) //
				.mapToObj(index -> "{" + index + "}") //
				.collect(joining(", "));
	}

	private Object[] makeReadable(MessageFormat format, Object[] arguments) {
		Format[] formats = format.getFormatsByArgumentIndex();
		Object[] result = Arrays.copyOf(arguments, Math.min(arguments.length, formats.length), Object[].class);
		for (int i = 0; i < result.length; i++) {
			if (formats[i] == null) {
				result[i] = truncateIfExceedsMaxLength(StringUtils.nullSafeToString(arguments[i]));
			}
		}
		return result;
	}

	private String truncateIfExceedsMaxLength(String argument) {
		if (argument != null && argument.length() > argumentMaxLength) {
			return argument.substring(0, argumentMaxLength - 1) + ELLIPSIS;
		}
		return argument;
	}

}
