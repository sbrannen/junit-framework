/*
 * Copyright 2015-2026 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.commons.support;

import java.net.URI;
import java.util.Objects;

import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ToStringBuilder;

/**
 * Default implementation of {@link Resource}.
 *
 * @since 1.11
 */
@SuppressWarnings("deprecation")
class DefaultResource implements Resource {

	private final String name;
	private final URI uri;

	DefaultResource(String name, URI uri) {
		this.name = Preconditions.notNull(name, "name must not be null");
		this.uri = Preconditions.notNull(uri, "uri must not be null");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public URI getUri() {
		return uri;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof org.junit.platform.commons.io.Resource) {
			org.junit.platform.commons.io.Resource that = (org.junit.platform.commons.io.Resource) obj;
			return this.name.equals(that.getName()) //
					&& this.uri.equals(that.getUri());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, uri);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this) //
				.append("name", name) //
				.append("uri", uri) //
				.toString();
	}
}
