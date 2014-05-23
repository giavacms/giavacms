/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class ProductMixin {
	@JsonCreator
	public ProductMixin(@JsonProperty("id") String id,
			@JsonProperty("title") String title,
			@JsonProperty("formerTitle") String formerTitle,
			@JsonProperty("description") String description,
			@JsonProperty("preview") String preview) {
	}

}
