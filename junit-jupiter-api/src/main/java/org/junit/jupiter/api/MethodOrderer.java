/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import org.apiguardian.api.API;
import org.junit.platform.commons.util.AnnotationUtils;

/**
 * @since 5.4
 */
@API(status = EXPERIMENTAL, since = "5.4")
public interface MethodOrderer {

	void orderMethods(List<? extends MethodDescriptor> methodDescriptors);

	interface MethodDescriptor {

		Class<?> getTestClass();

		Method getTestMethod();

	}

	/**
	 * {@code MethodOrderer} that sorts methods alphanumerically.
	 */
	class Alphanumeric implements MethodOrderer {

		private static final Comparator<MethodDescriptor> comparator = new AlphanumericComparator();

		@Override
		public void orderMethods(List<? extends MethodDescriptor> methodDescriptors) {
			methodDescriptors.sort(comparator);
		}

		private static final class AlphanumericComparator implements Comparator<MethodDescriptor> {

			@Override
			public int compare(MethodDescriptor descriptor1, MethodDescriptor descriptor2) {
				Method method1 = descriptor1.getTestMethod();
				Method method2 = descriptor2.getTestMethod();

				int result = method1.getName().compareTo(method2.getName());
				return (result != 0) ? result : method1.toString().compareTo(method2.toString());
			}
		}
	}

	/**
	 * {@code MethodOrderer} that supports the {@link Order @Order} annotation.
	 */
	class OrderAnnotation implements MethodOrderer {

		private static final Comparator<MethodDescriptor> comparator = new OrderAnnotationComparator();

		@Override
		public void orderMethods(List<? extends MethodDescriptor> methodDescriptors) {
			methodDescriptors.sort(comparator);
		}

		private static final class OrderAnnotationComparator implements Comparator<MethodDescriptor> {

			@Override
			public int compare(MethodDescriptor descriptor1, MethodDescriptor descriptor2) {
				return Integer.compare(getOrder(descriptor1), getOrder(descriptor2));
			}

			private Integer getOrder(MethodDescriptor descriptor) {
				return AnnotationUtils.findAnnotation(descriptor.getTestMethod(), Order.class)//
						.map(Order::value)//
						.orElse(Integer.MAX_VALUE);
			}
		}
	}

}
