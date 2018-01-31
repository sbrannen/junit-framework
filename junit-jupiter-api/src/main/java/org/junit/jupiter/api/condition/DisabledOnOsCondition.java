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

import static org.junit.jupiter.api.condition.EnabledOnOsCondition.DISABLED_ON_CURRENT_OS;
import static org.junit.jupiter.api.condition.EnabledOnOsCondition.ENABLED_ON_CURRENT_OS;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * {@link ExecutionCondition} for {@link DisabledOnOs @DisabledOnOs}.
 *
 * @since 5.1
 * @see DisabledOnOs
 */
class DisabledOnOsCondition implements ExecutionCondition {

	private static final ConditionEvaluationResult ENABLED_BY_DEFAULT = enabled("@DisabledOnOs is not present");

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
		Optional<DisabledOnOs> optional = findAnnotation(context.getElement(), DisabledOnOs.class);
		if (optional.isPresent()) {
			return (Arrays.stream(optional.get().value()).anyMatch(OS::isCurrentOs)) ? DISABLED_ON_CURRENT_OS
					: ENABLED_ON_CURRENT_OS;
		}
		return ENABLED_BY_DEFAULT;
	}

}
