/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.engine.discovery;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.MethodSelector;

/**
 * Jupiter-specific selector for methods, potentially in nested classes.
 *
 * <p>The important difference to {@link MethodSelector} is that this selector's
 * {@link #equals(Object)} method takes into account the selected method's
 * {@linkplain Method#getDeclaringClass() declaring class} to support cases
 * where a package-private method is declared in a super class in a different
 * package and a method with the same signature is declared in a subclass. In
 * that case both methods should be discovered because the one declared in the
 * subclass does <em>not</em> override the one in the super class.
 *
 * @since 6.0.1
 */
final class DeclaredMethodSelector implements DiscoverySelector {

	private final List<Class<?>> testClasses;
	private final Method method;

	DeclaredMethodSelector(List<Class<?>> testClasses, Method method) {
		Preconditions.notEmpty(testClasses, "testClasses must not be empty");
		this.testClasses = Preconditions.containsNoNullElements(testClasses,
			"testClasses must not contain null elements");
		this.method = Preconditions.notNull(method, "method must not be null");
	}

	List<Class<?>> testClasses() {
		return testClasses;
	}

	Method method() {
		return method;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DeclaredMethodSelector)) {
			return false;
		}
		DeclaredMethodSelector that = (DeclaredMethodSelector) o;
		return Objects.equals(testClasses, that.testClasses) //
				&& Objects.equals(method, that.method);
	}

	@Override
	public int hashCode() {
		return Objects.hash(testClasses, method);
	}
}
