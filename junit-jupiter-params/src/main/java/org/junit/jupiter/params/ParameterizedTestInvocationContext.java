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

import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.params.provider.Arguments;

/**
 * @since 5.0
 */
class ParameterizedTestInvocationContext implements TestTemplateInvocationContext {

	private final ParameterizedTestNameFormatter formatter;
	private final ParameterizedTestMethodContext methodContext;
	private final Arguments arguments;
	private final int invocationIndex;

	ParameterizedTestInvocationContext(ParameterizedTestNameFormatter formatter,
			ParameterizedTestMethodContext methodContext, Arguments arguments, int invocationIndex) {
		this.formatter = formatter;
		this.methodContext = methodContext;
		this.arguments = arguments;
		this.invocationIndex = invocationIndex;
	}

	@Override
	public String getDisplayName(int invocationIndex) {
		return this.formatter.format(invocationIndex, this.arguments);
	}

	@Override
	public List<Extension> getAdditionalExtensions() {
		return singletonList(
			new ParameterizedTestParameterResolver(this.methodContext, consumedArguments(), this.invocationIndex));
	}

	private Object[] consumedArguments() {
		Object[] args = this.arguments.get();
		if (this.methodContext.hasAggregator()) {
			return args;
		}
		int parameterCount = this.methodContext.getParameterCount();
		return args.length > parameterCount ? Arrays.copyOf(args, parameterCount) : args;
	}

}
