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

import java.lang.reflect.Method;

import org.apiguardian.api.API;
import org.junit.platform.commons.util.ReflectionUtils;

/**
 * Enumeration of Java Runtime Environment versions.
 *
 * @since 5.1
 * @see EnabledOnJre
 * @see DisabledOnJre
 */
@API(status = STABLE, since = "5.1")
public enum JRE {

	/**
	 * Java 8.
	 */
	JAVA_8,

	/**
	 * Java 9.
	 */
	JAVA_9,

	/**
	 * Java 10.
	 */
	JAVA_10,

	/**
	 * Java 11.
	 */
	JAVA_11,

	/**
	 * A JRE version other than {@link #JAVA_8}, {@link #JAVA_9},
	 * {@link #JAVA_10}, or {@link #JAVA_11}.
	 */
	OTHER;

	private static final JRE CURRENT_VERSION = determineCurrentVersion();

	private static JRE determineCurrentVersion() {
		String javaVersion = System.getProperty("java.version");
		// TODO Log DEBUG if "JVM system property 'java.version' is undefined"
		if (javaVersion != null && javaVersion.startsWith("1.8")) {
			return JAVA_8;
		}
		else {
			try {
				// java.lang.Runtime.version() is a static method available on Java 9+
				// that returns an instance of java.lang.Runtime.Version which has the
				// following method: public int major()
				Method versionMethod = Runtime.class.getMethod("version");
				Object version = ReflectionUtils.invokeMethod(versionMethod, null);
				Method majorMethod = version.getClass().getMethod("major");
				int major = (int) ReflectionUtils.invokeMethod(majorMethod, version);
				switch (major) {
					case 9:
						return JAVA_9;
					case 10:
						return JAVA_10;
					case 11:
						return JAVA_11;
				}
			}
			catch (Exception ex) {
				// TODO Log DEBUG "Failed to determine the current JRE version"
			}
			return OTHER;
		}
	}

	/**
	 * @return {@code true} if <em>this</em> {@code JRE} is the Java Runtime
	 * Environment version for the currently executing JVM.
	 */
	public boolean isCurrentVersion() {
		return this == CURRENT_VERSION;
	}

}
