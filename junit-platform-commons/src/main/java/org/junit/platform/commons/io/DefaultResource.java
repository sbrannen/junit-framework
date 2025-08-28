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

import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ToStringBuilder;

/**
 * Default implementation of {@link Resource}.
 *
 * @since 6.0
 */
record DefaultResource(String name, URI uri) implements Resource {

	DefaultResource {
		Preconditions.notNull(name, "name must not be null");
		Preconditions.notNull(uri, "uri must not be null");
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
	public String toString() {
		return new ToStringBuilder(this) //
				.append("name", this.name) //
				.append("uri", this.uri) //
				.toString();
	}

}
