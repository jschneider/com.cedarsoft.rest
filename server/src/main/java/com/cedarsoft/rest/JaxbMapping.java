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

import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.jaxb.JaxbStub;
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
 * @param <S> the Jaxb stub type
 */
public abstract class JaxbMapping<T, J extends JaxbObject, S extends JaxbStub<J>> {
  @NotNull
  private final Map<T, J> jaxbObjects = new HashMap<T, J>();
  @NotNull
  private final Map<T, S> jaxbStubObjects = new HashMap<T, S>();
  @NotNull
  private final DelegateJaxbMappings delegateJaxbMappings = new DelegateJaxbMappings();

  @NotNull
  public DelegateJaxbMappings getDelegatesMapping() {
    return delegateJaxbMappings;
  }

  @NotNull
  public S getJaxbObjectStub( @NotNull T object, @Nullable UriBuilder uriBuilder ) throws URISyntaxException {
    S jaxbObject = jaxbStubObjects.get( object );
    if ( jaxbObject != null ) {
      return jaxbObject;
    }

    JaxbMappingContext context = new JaxbMappingContext( uriBuilder, delegateJaxbMappings );

    S created = createJaxbObjectStub( object, context );
    jaxbStubObjects.put( object, created );

    if ( uriBuilder != null ) {
      setUris( created, uriBuilder.clone() );
    }

    return created;

  }

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
  protected <T, J extends JaxbObject> J get( @NotNull Class<J> jaxbType, @Nullable T object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    if ( object == null ) {
      return null;
    }
    //    return getDelegatesMapping().getMapping( jaxbType ).getJaxbObject( object, context.getUriBuilder() );
    return getDelegatesMapping().getMapping( jaxbType ).getJaxbObject( object, context.getUriBuilder() );
  }

  @Nullable
  protected <T, J extends JaxbObject> List<J> get( @NotNull Class<J> jaxbType, @NotNull Iterable<? extends T> objects, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    JaxbMapping<Object, J, ? extends JaxbStub<J>> mapping = getDelegatesMapping().getMapping( jaxbType );

    List<J> converted = new ArrayList<J>();
    for ( T object : objects ) {
      //      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
    }

    return converted;
  }

  @Nullable
  protected <T, J extends JaxbObject, S extends JaxbStub<J>> S getStub( @NotNull Class<S> jaxbStubType, @Nullable T object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    if ( object == null ) {
      return null;
    }
    return getDelegatesMapping().getMappingForStub( jaxbStubType ).getJaxbObjectStub( object, context.getUriBuilder() );
  }

  @Nullable
  protected <T, J extends JaxbObject, S extends JaxbStub<J>> List<S> getStub( @NotNull Class<S> jaxbSubType, @NotNull Iterable<? extends T> objects, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    JaxbMapping<Object, ?, S> mapping = getDelegatesMapping().getMappingForStub( jaxbSubType );

    List<S> converted = new ArrayList<S>();
    for ( T object : objects ) {
      //      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
      converted.add( mapping.getJaxbObjectStub( object, context.getUriBuilder() ) );
    }

    return converted;
  }

  /**
   * Sets the URIs for the given object.
   * You have to call *clone()* before every usage of the uriBuilder!!!
   *
   * @param object     the object (of type J or S)
   * @param uriBuilder the uri builder
   */
  protected abstract void setUris( @NotNull JaxbObject object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException;

  @NotNull
  public List<J> getJaxbObjects( @NotNull Iterable<? extends T> objects, @Nullable UriBuilder uriBuilder ) throws URISyntaxException {
    List<J> currentJaxbObjects = new ArrayList<J>();
    for ( T object : objects ) {
      currentJaxbObjects.add( getJaxbObject( object, uriBuilder ) );
    }
    return currentJaxbObjects;
  }

  @NotNull
  public List<S> getJaxbStubs( @NotNull Iterable<? extends T> objects, @Nullable UriBuilder uriBuilder ) throws URISyntaxException {
    List<S> currentJaxbObjects = new ArrayList<S>();
    for ( T object : objects ) {
      currentJaxbObjects.add( getJaxbObjectStub( object, uriBuilder ) );
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

  /**
   * Creates the JaxbObjectStub.
   * <p/>
   * A stub only contains the very basic informations off the object. It is used in collections and when referenced
   * from other objects.
   * <p/>
   * It has to contain a href to fetch the missing details later
   *
   * @param object  the object
   * @param context the context
   * @return the stub
   *
   * @throws URISyntaxException
   */
  protected abstract S createJaxbObjectStub( @NotNull T object, @NotNull JaxbMappingContext context ) throws URISyntaxException;
}
