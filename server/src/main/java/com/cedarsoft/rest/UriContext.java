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

import javax.annotation.Nonnull;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Represents the URI context.
 * <p/>
 * There is a local context ({@link #getUriBuilder()}) which represents the current position.
 * And there is access to the base uri using {@link #getBaseUriBuilder()}.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UriContext {
  @Nonnull
  private final UriBuilder baseUriBuilder;
  @Nonnull
  private final UriBuilder uriBuilder;

  public UriContext( @Nonnull UriBuilder baseUriBuilder, @Nonnull UriBuilder uriBuilder ) {
    this.baseUriBuilder = baseUriBuilder.clone();
    this.uriBuilder = uriBuilder.clone();
  }

  /**
   * Returns a clone of the base uri builder
   *
   * @return the base uri builder
   */
  @Nonnull
  public UriBuilder getBaseUriBuilder() {
    return baseUriBuilder.clone();
  }

  /**
   * Returns the uri builder for the local context
   *
   * @return the uri builder
   */
  @Nonnull
  public UriBuilder getUriBuilder() {
    return uriBuilder.clone();
  }

  /**
   * Returns the URI
   *
   * @return the URI
   */
  @Nonnull
  public URI getUri() {
    return uriBuilder.build();
  }

  @Nonnull
  public UriContext create( @Nonnull UriBuilder newUriBuilder ) {
    return new UriContext( baseUriBuilder, newUriBuilder );
  }
}
