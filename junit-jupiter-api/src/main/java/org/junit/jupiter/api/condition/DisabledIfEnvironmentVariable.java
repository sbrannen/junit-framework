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

import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @since 5.1
 * @see EnabledIfEnvironmentVariable
 * @see org.junit.jupiter.api.Disabled
 * @see org.junit.jupiter.api.EnabledIf
 * @see org.junit.jupiter.api.DisabledIf
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(DisabledIfEnvironmentVariableCondition.class)
@API(status = STABLE, since = "5.1")
public @interface DisabledIfEnvironmentVariable {

	/**
	 * The name of the environment variable to retrieve.
	 */
	String named();

	/**
	 * A regular expression that will be used to match against the retrieved
	 * value of the {@link #named} environment variable.
	 */
	String matches();

}
