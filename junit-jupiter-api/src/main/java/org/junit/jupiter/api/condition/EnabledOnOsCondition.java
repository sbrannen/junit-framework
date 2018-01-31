/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api.condition;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * {@link ExecutionCondition} for {@link EnabledOnOs @EnabledOnOs}.
 *
 * @since 5.1
 * @see EnabledOnOs
 */
class EnabledOnOsCondition implements ExecutionCondition {

	private static final ConditionEvaluationResult ENABLED_BY_DEFAULT = enabled("@EnabledOnOs is not present");

	static final ConditionEvaluationResult ENABLED_ON_CURRENT_OS = //
		enabled("Enabled on operating system: " + System.getProperty("os.name"));

	static final ConditionEvaluationResult DISABLED_ON_CURRENT_OS = //
		disabled("Disabled on operating system: " + System.getProperty("os.name"));

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
		Optional<EnabledOnOs> optional = findAnnotation(context.getElement(), EnabledOnOs.class);
		if (optional.isPresent()) {
			return (Arrays.stream(optional.get().value()).anyMatch(OS::isCurrentOs)) ? ENABLED_ON_CURRENT_OS
					: DISABLED_ON_CURRENT_OS;
		}
		return ENABLED_BY_DEFAULT;
	}

}
