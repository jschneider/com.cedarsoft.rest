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
  public S getJaxbObjectStub( @NotNull T object, @NotNull UriContext uriContext ) throws URISyntaxException {
    S jaxbObject = jaxbStubObjects.get( object );
    if ( jaxbObject != null ) {
      return jaxbObject;
    }

    JaxbMappingContext context = new JaxbMappingContext( uriContext, delegateJaxbMappings );

    S created = createJaxbObjectStub( object, context );
    jaxbStubObjects.put( object, created );


    return created;

  }

  public J getJaxbObject( @NotNull T object, @NotNull UriContext uriContext ) throws URISyntaxException {
    J jaxbObject = jaxbObjects.get( object );
    if ( jaxbObject != null ) {
      return jaxbObject;
    }

    JaxbMappingContext context = new JaxbMappingContext( uriContext, delegateJaxbMappings );

    J created = createJaxbObject( object, context );
    jaxbObjects.put( object, created );

    return created;
  }

  @Nullable
  protected <T, J extends JaxbObject> J get( @NotNull Class<J> jaxbType, @Nullable T object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    if ( object == null ) {
      return null;
    }
    //    return getDelegatesMapping().getMapping( jaxbType ).getJaxbObject( object, context.getUriBuilder() );
    return getDelegatesMapping().getMapping( jaxbType ).getJaxbObject( object, context.getUriContext() );
  }

  @Nullable
  protected <T, J extends JaxbObject> List<J> get( @NotNull Class<J> jaxbType, @NotNull Iterable<? extends T> objects, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    JaxbMapping<Object, J, ? extends JaxbStub<J>> mapping = getDelegatesMapping().getMapping( jaxbType );

    List<J> converted = new ArrayList<J>();
    for ( T object : objects ) {
      //      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
      converted.add( mapping.getJaxbObject( object, context.getUriContext() ) );
    }

    return converted;
  }

  @Nullable
  protected <T, J extends JaxbObject, S extends JaxbStub<J>> S getStub( @NotNull Class<S> jaxbStubType, @Nullable T object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    if ( object == null ) {
      return null;
    }
    return getDelegatesMapping().getMappingForStub( jaxbStubType ).getJaxbObjectStub( object, context.getUriContext() );
  }

  @Nullable
  protected <T, J extends JaxbObject, S extends JaxbStub<J>> List<S> getStub( @NotNull Class<S> jaxbSubType, @NotNull Iterable<? extends T> objects, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    JaxbMapping<Object, ?, S> mapping = getDelegatesMapping().getMappingForStub( jaxbSubType );

    List<S> converted = new ArrayList<S>();
    for ( T object : objects ) {
      //      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
      converted.add( mapping.getJaxbObjectStub( object, context.getUriContext() ) );
    }

    return converted;
  }

  @NotNull
  public List<J> getJaxbObjects( @NotNull Iterable<? extends T> objects, @NotNull UriContext uriContext ) throws URISyntaxException {
    List<J> currentJaxbObjects = new ArrayList<J>();
    for ( T object : objects ) {
      currentJaxbObjects.add( getJaxbObject( object, uriContext ) );
    }
    return currentJaxbObjects;
  }

  @NotNull
  public List<S> getJaxbStubs( @NotNull Iterable<? extends T> objects, @NotNull UriContext uriContext ) throws URISyntaxException {
    List<S> currentJaxbObjects = new ArrayList<S>();
    for ( T object : objects ) {
      currentJaxbObjects.add( getJaxbObjectStub( object, uriContext ) );
    }
    return currentJaxbObjects;
  }

  /**
   * Creates the local context.
   * The local context has an updated URI context.
   *
   * @param context    the base context
   * @param jaxbObject the jaxb object
   * @return the local context (with updated URI builder)
   */
  @NotNull
  protected JaxbMappingContext createLocalContext( @NotNull JaxbMappingContext context, @NotNull JaxbObject jaxbObject ) {
    assert jaxbObject.getId() != null;
    return context.create( getUri( jaxbObject, context.getUriContext() ) );
  }

  @NotNull
  protected J createJaxbObject( @NotNull T object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    J jaxbObject = createJaxbObject( object );

    JaxbMappingContext localContext = createLocalContext( context, jaxbObject );
    setHref( jaxbObject, localContext );

    copyFields( object, jaxbObject, localContext );
    return jaxbObject;
  }

  /**
   * Sets the URI to the jaxb object.
   * Override this method for custom behaviour (e.g. no href set)
   *
   * @param jaxbObject   the jaxb object
   * @param uriContext the local context that is used to get the URI
   */
  protected void setHref( @NotNull JaxbObject jaxbObject, @NotNull UriContext uriContext ) {
    jaxbObject.setHref( uriContext.getUri() );
  }

  /**
   * Returns the local URI for the object
   *
   * @param object     the object (may be used to fetch the ID)
   * @param uriContext the uri context (offering access to both the local and base URI
   * @return the updated uri builder
   */
  @NotNull
  protected abstract UriBuilder getUri( @NotNull JaxbObject object, @NotNull UriContext uriContext );

  /**
   * Copy the fields from the source
   *
   * @param source  the source model
   * @param target  the target jaxb object
   * @param context the context
   * @throws URISyntaxException
   */
  protected abstract void copyFields( @NotNull T source, @NotNull J target, @NotNull JaxbMappingContext context ) throws URISyntaxException;

  @NotNull
  protected abstract J createJaxbObject( @NotNull T object );

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
