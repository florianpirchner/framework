/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.osgi.servlet.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.osgi.namespace.implementation.ImplementationNamespace;

import aQute.bnd.annotation.headers.RequireCapability;

/**
 * Require an implementation for the Vaadin OSGi provider.
 * 
 * @author Florian Pirchner
 *
 * @since 8.2
 */
@RequireCapability(ns = ImplementationNamespace.IMPLEMENTATION_NAMESPACE, filter = "(&("
		+ ImplementationNamespace.IMPLEMENTATION_NAMESPACE + "=" + Constants.OSGI_PROVIDER_NAME
		+ ")${frange;${version;==;" + Constants.OSGI_PROVIDER_VERSION + "}})")
@Retention(RetentionPolicy.CLASS)
public @interface RequireVaadinOSGiServletProvider {

}