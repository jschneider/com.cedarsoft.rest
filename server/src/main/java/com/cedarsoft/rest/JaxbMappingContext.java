/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.rest;

import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Context object for JAXB Mappings
 */
public class JaxbMappingContext {
  @NotNull
  private final UriContext uriContext;
  @NotNull
  private final DelegateJaxbMappings delegateJaxbMappings;

  @Deprecated
  public JaxbMappingContext( @NotNull UriBuilder baseUriBuilder, @NotNull UriBuilder uriBuilder, @NotNull DelegateJaxbMappings delegateJaxbMappings ) {
    this( new UriContext( baseUriBuilder, uriBuilder ), delegateJaxbMappings );
  }

  /**
   * Generates a new mapping context.
   *
   * @param uriContext           the uri context
   * @param delegateJaxbMappings the delegate mappings
   */
  public JaxbMappingContext( @NotNull UriContext uriContext, @NotNull DelegateJaxbMappings delegateJaxbMappings ) {
    this.uriContext = uriContext;
    this.delegateJaxbMappings = delegateJaxbMappings;
  }

  @NotNull
  public UriContext getUriContext() {
    return uriContext;
  }

  @NotNull
  public DelegateJaxbMappings getDelegateJaxbMappings() {
    return delegateJaxbMappings;
  }

  /**
   * Creates a new context based on the changed uri builder
   *
   * @param newUriBuilder the updated uri builder
   * @return a new context with the new uri builder and the old delegate jaxb mappings
   */
  @NotNull
  public JaxbMappingContext create( @NotNull UriBuilder newUriBuilder ) {
    return new JaxbMappingContext( uriContext.create( newUriBuilder ), delegateJaxbMappings );
  }
}
