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

import java.util.Locale;

import org.apiguardian.api.API;
import org.junit.platform.commons.util.Preconditions;

/**
 * Enumeration of common operating systems used for testing Java applications.
 *
 * @since 5.1
 * @see EnabledOnOs
 * @see DisabledOnOs
 */
@API(status = STABLE, since = "5.1")
public enum OS {

	/**
	 * Linux-based operating system.
	 */
	LINUX,

	/**
	 * Apple Macintosh operating system (i.e., mac OS).
	 */
	MAC,

	/**
	 * Oracle Solaris operating system.
	 */
	SOLARIS,

	/**
	 * Microsoft Windows operating system.
	 */
	WINDOWS,

	/**
	 * An operating system other than {@link #LINUX}, {@link #MAC},
	 * {@link #SOLARIS}, or {@link #WINDOWS}.
	 */
	OTHER;

	private static final OS CURRENT_OS;

	static {
		String os = System.getProperty("os.name");
		Preconditions.notBlank(os, "JVM system property 'os.name' is undefined");
		os = os.toLowerCase(Locale.ENGLISH);

		if (os.contains("linux")) {
			CURRENT_OS = LINUX;
		}
		else if (os.contains("mac")) {
			CURRENT_OS = MAC;
		}
		else if (os.contains("solaris")) {
			CURRENT_OS = SOLARIS;
		}
		else if (os.contains("win")) {
			CURRENT_OS = WINDOWS;
		}
		else {
			CURRENT_OS = OTHER;
		}
	}

	/**
	 * @return {@code true} if <em>this</em> {@code OS} is the operating system
	 * on which the current JVM is executing
	 */
	public boolean isCurrentOs() {
		return this == CURRENT_OS;
	}

}
