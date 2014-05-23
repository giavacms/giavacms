/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.rest;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

abstract class ProductMixin {
	@JsonCreator
	public ProductMixin(@JsonProperty("id") String id,
			@JsonProperty("title") String title,
			@JsonProperty("formerTitle") String formerTitle,
			@JsonProperty("description") String description,
			@JsonProperty("preview") String preview) {
	}

}
