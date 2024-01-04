/*
 * Copyright 2015-2023 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.engine.extension;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.cause;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.engine.AbstractJupiterTestEngineTests;
import org.junit.platform.testkit.engine.Events;

/**
 * Integration tests for the behavior of the
 * {@link org.junit.jupiter.api.AutoCloseExtension} to release resources after
 * test execution.
 *
 * @since 5.11
 */
class AutoCloseTests extends AbstractJupiterTestEngineTests {

	private static final List<String> recorder = new ArrayList<>();

	@BeforeEach
	void resetRecorder() {
		recorder.clear();
	}

	@Test
	void fieldsAreProperlyClosed() {
		Events tests = executeTestsForClass(AutoCloseTestCase.class).testEvents();
		tests.assertStatistics(stats -> stats.succeeded(2));
		// @formatter:off
		assertThat(recorder).containsExactly(
				"afterEach-close()", "afterEach-run()",
				"afterEach-close()", "afterEach-run()",
						"afterAll-close()");
		// @formatter:on
	}

	@Test
	void noCloseMethod() {
		String msg = "@AutoClose failed to close object for field "
				+ "org.junit.jupiter.engine.extension.AutoCloseTests$AutoCloseNoCloseMethodFailingTestCase.field "
				+ "because the close() method could not be resolved.";

		Events tests = executeTestsForClass(AutoCloseNoCloseMethodFailingTestCase.class).testEvents();
		assertFailingWithMessage(tests, msg);
	}

	@Test
	void noShutdownMethod() {
		String msg = "@AutoClose failed to close object for field "
				+ "org.junit.jupiter.engine.extension.AutoCloseTests$AutoCloseNoShutdownMethodFailingTestCase.field "
				+ "because the shutdown() method could not be resolved.";

		Events tests = executeTestsForClass(AutoCloseNoShutdownMethodFailingTestCase.class).testEvents();
		assertFailingWithMessage(tests, msg);
	}

	@Test
	void spyPermitsOnlyASingleAction() {
		AutoCloseSpy spy = new AutoCloseSpy("");

		spy.close();

		assertThrows(IllegalStateException.class, spy::close);
		assertThrows(IllegalStateException.class, spy::run);
		assertEquals(asList("close()"), recorder);
	}

	@Test
	void instancePerClass() {
		Events tests = executeTestsForClass(AutoCloseInstancePerClassTestCase.class).testEvents();
		tests.assertStatistics(stats -> stats.succeeded(2));
	}

	private static void assertFailingWithMessage(Events testEvent, String msg) {
		testEvent.assertStatistics(stats -> stats.failed(1)).assertThatEvents().haveExactly(1,
			finishedWithFailure(cause(message(actual -> actual.contains(msg)))));
	}

	static class AutoCloseTestCase {

		@AutoClose
		private static AutoCloseable staticClosable;
		@AutoClose
		private static AutoCloseable nullStatic;

		@AutoClose
		private final AutoCloseable closable = new AutoCloseSpy("afterEach-");
		@AutoClose("run")
		private final Runnable runnable = new AutoCloseSpy("afterEach-");
		@AutoClose
		private AutoCloseable nullField;

		@BeforeAll
		static void setup() {
			staticClosable = new AutoCloseSpy("afterAll-");
		}

		@Test
		void justPass() {
			assertFields();
		}

		@Test
		void anotherPass() {
			assertFields();
		}

		private void assertFields() {
			assertNotNull(staticClosable);
			assertNull(nullStatic);

			assertNotNull(closable);
			assertNotNull(runnable);
			assertNull(nullField);
		}

	}

	static class AutoCloseNoCloseMethodFailingTestCase {

		@AutoClose
		private final String field = "nothing to close()";

		@Test
		void alwaysPass() {
			assertNotNull(field);
		}

	}

	static class AutoCloseNoShutdownMethodFailingTestCase {

		@AutoClose("shutdown")
		private final String field = "nothing to shutdown()";

		@Test
		void alwaysPass() {
			assertNotNull(field);
		}

	}

	@TestInstance(PER_CLASS)
	static class AutoCloseInstancePerClassTestCase {

		static boolean closed;

		@AutoClose
		AutoCloseable field = () -> closed = true;

		@Test
		void test1() {
			assertFalse(closed);
		}

		@Test
		void test2() {
			assertFalse(closed);
		}

	}

	static class AutoCloseSpy implements AutoCloseable, Runnable {

		private final String prefix;
		private String invokedMethod = "";

		public AutoCloseSpy(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public void run() {
			checkIfAlreadyInvoked();
			recordInvocation("run()");
		}

		@Override
		public void close() {
			checkIfAlreadyInvoked();
			recordInvocation("close()");
		}

		private void checkIfAlreadyInvoked() {
			if (!invokedMethod.isEmpty()) {
				throw new IllegalStateException();
			}
		}

		private void recordInvocation(String methodName) {
			invokedMethod = methodName;
			recorder.add(prefix + methodName);
		}

	}

}
