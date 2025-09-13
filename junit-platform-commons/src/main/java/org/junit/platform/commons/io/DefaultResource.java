/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.commons.io;

import java.net.URI;
import java.util.Objects;

import org.junit.platform.commons.PreconditionViolationException;

/**
 * Default implementation of {@link Resource}.
 *
 * @since 6.0
 */
class DefaultResource implements Resource {

	private final String name;
	private final URI uri;

	DefaultResource(String name, URI uri) {
		this.name = checkNotNull(name, "name");
		this.uri = checkNotNull(uri, "uri");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URI getUri() {
		return this.uri;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Resource) {
			Resource that = (Resource) obj;
			return this.name.equals(that.getName()) //
					&& this.uri.equals(that.getUri());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, uri);
	}

	// Cannot use Preconditions due to package cycle
	private static <T> T checkNotNull(T input, String title) {
		if (input == null) {
			throw new PreconditionViolationException(title + " must not be null");
		}
		return input;
	}

}
