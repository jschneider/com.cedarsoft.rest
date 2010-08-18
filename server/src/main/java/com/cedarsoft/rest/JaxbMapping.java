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

import com.cedarsoft.jaxb.AbstractJaxbObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T> the type
 * @param <J> the Jaxb type
 */
public abstract class JaxbMapping<T, J> {
  @NotNull
  private final Map<T, J> jaxbObjects = new HashMap<T, J>();
  @NotNull
  private final DelegateJaxbMappings delegateJaxbMappings = new DelegateJaxbMappings();

  @NotNull
  public DelegateJaxbMappings getDelegatesMapping() {
    return delegateJaxbMappings;
  }

  @NotNull
  public J getJaxbObject( @NotNull T object, @Nullable UriBuilder uriBuilder ) throws URISyntaxException {
    J jaxbObject = jaxbObjects.get( object );
    if ( jaxbObject != null ) {
      return jaxbObject;
    }

    JaxbMappingContext context = new JaxbMappingContext( uriBuilder, delegateJaxbMappings );

    J created = createJaxbObject( object, context );
    jaxbObjects.put( object, created );

    if ( uriBuilder != null ) {
      setUris( created, uriBuilder.clone() );
    }

    return created;
  }

  @Nullable
  protected <T, J extends AbstractJaxbObject> J get( @NotNull Class<J> jaxbType, @Nullable T object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    if ( object == null ) {
      return null;
    }
    return getDelegatesMapping().getMapping( jaxbType ).getJaxbObject( object, context.getUriBuilder() );
  }

  @Nullable
  protected <T, J extends AbstractJaxbObject> List<J> get( @NotNull Class<J> jaxbType, @NotNull Iterable<? extends T> objects, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    JaxbMapping<Object, J> mapping = getDelegatesMapping().getMapping( jaxbType );

    List<J> converted = new ArrayList<J>();
    for ( T object : objects ) {
      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
    }

    return converted;
  }

  /**
   * Sets the URIs for the given object.
   * You have to call *clone()* before every usage of the uriBuilder!!!
   *
   * @param object     the object
   * @param uriBuilder the uri builder
   */
  protected abstract void setUris( @NotNull J object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException;

  @NotNull
  public List<J> getJaxbObjects( @NotNull Iterable<? extends T> objects, @Nullable UriBuilder uriBuilder ) throws URISyntaxException {
    List<J> currentJaxbObjects = new ArrayList<J>();
    for ( T object : objects ) {
      currentJaxbObjects.add( getJaxbObject( object, uriBuilder ) );
    }
    return currentJaxbObjects;
  }

  /**
   * Creates the JaxbObject.
   * The UriBuilder should only be used
   *
   * @param object  the object
   * @param context the context that can be used to create other objects
   * @return the created jaxb object
   */
  @NotNull
  protected abstract J createJaxbObject( @NotNull T object, @NotNull JaxbMappingContext context ) throws URISyntaxException;
}
