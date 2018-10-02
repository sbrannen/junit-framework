/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.engine.discovery;

import java.lang.reflect.Method;

import org.junit.jupiter.engine.descriptor.MethodBasedTestDescriptor;
import org.junit.platform.commons.util.ToStringBuilder;

/**
 * @since 5.4
 */
// TODO Move MethodDescriptor to new extension SPI, and convert to interface.
public class MethodDescriptor {

	private final MethodBasedTestDescriptor testDescriptor;

	MethodDescriptor(MethodBasedTestDescriptor testDescriptor) {
		this.testDescriptor = testDescriptor;
	}

	public MethodBasedTestDescriptor getTestDescriptor() {
		return testDescriptor;
	}

	public final Class<?> getTestClass() {
		return this.testDescriptor.getTestClass();
	}

	public final Method getTestMethod() {
		return this.testDescriptor.getTestMethod();
	}

	@Override
	public String toString() {
		// @formatter:off
		return new ToStringBuilder(this)
				.append("testClass", getTestClass().getName())
				.append("testMethod", getTestMethod().toGenericString())
				.toString();
		// @formatter:on
	}

}
