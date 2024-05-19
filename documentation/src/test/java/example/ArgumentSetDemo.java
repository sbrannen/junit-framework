/*
 * Copyright 2015-2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package example;

import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

@Execution(SAME_THREAD)
class ArgumentSetDemo {

	@BeforeEach
	void printDisplayName(TestInfo testInfo) {
		System.out.println(testInfo.getDisplayName());
	}

	@ParameterizedTest
	@FieldSource("argumentSets")
	void defaultArgumentSetDisplayName(File file1, File file2) {
	}

	@ParameterizedTest(name = "{argumentSetName} :: {arguments}")
	@FieldSource("argumentSets")
	void customArgumentSetDisplayName(File file1, File file2) {
	}

	// @formatter:off
	static List<Arguments> argumentSets = Arrays.asList(
		argumentSet("Important Files", new File("path1"), new File("path2")),
		argumentSet("Other Files", new File("path3"), new File("path4"))
	);
	// @formatter:on

}
