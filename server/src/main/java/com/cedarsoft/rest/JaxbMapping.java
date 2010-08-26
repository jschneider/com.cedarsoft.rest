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
  public S getJaxbObjectStub( @NotNull T object, @NotNull UriContext context ) {
    S jaxbObject = jaxbStubObjects.get( object );
    if ( jaxbObject != null ) {
      return jaxbObject;
    }

    S created = createJaxbObjectStub( object, context );
    jaxbStubObjects.put( object, created );

    return created;
  }

  @NotNull
  public J getJaxbObject( @NotNull T object, @NotNull UriContext context ) {
    J jaxbObject = jaxbObjects.get( object );
    if ( jaxbObject != null ) {
      return jaxbObject;
    }

    J created = createJaxbObject( object, context );
    jaxbObjects.put( object, created );

    return created;
  }

  @Nullable
  protected <T, J extends JaxbObject> J get( @NotNull Class<J> jaxbType, @Nullable T object, @NotNull UriContext context ) {
    if ( object == null ) {
      return null;
    }
    //    return getDelegatesMapping().getMapping( jaxbType ).getJaxbObject( object, context.getUriBuilder() );
    return getDelegatesMapping().getMapping( jaxbType ).getJaxbObject( object, context );
  }

  @Nullable
  protected <T, J extends JaxbObject> List<J> get( @NotNull Class<J> jaxbType, @NotNull Iterable<? extends T> objects, @NotNull UriContext context ) {
    JaxbMapping<Object, J, ? extends JaxbStub<J>> mapping = getDelegatesMapping().getMapping( jaxbType );

    List<J> converted = new ArrayList<J>();
    for ( T object : objects ) {
      //      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
      converted.add( mapping.getJaxbObject( object, context ) );
    }

    return converted;
  }

  @Nullable
  protected <T, J extends JaxbObject, S extends JaxbStub<J>> S getStub( @NotNull Class<S> jaxbStubType, @Nullable T object, @NotNull UriContext context ) {
    if ( object == null ) {
      return null;
    }
    return getDelegatesMapping().getMappingForStub( jaxbStubType ).getJaxbObjectStub( object, context );
  }

  @Nullable
  protected <T, J extends JaxbObject, S extends JaxbStub<J>> List<S> getStub( @NotNull Class<S> jaxbSubType, @NotNull Iterable<? extends T> objects, @NotNull UriContext context ) {
    JaxbMapping<Object, ?, S> mapping = getDelegatesMapping().getMappingForStub( jaxbSubType );

    List<S> converted = new ArrayList<S>();
    for ( T object : objects ) {
      //      converted.add( mapping.getJaxbObject( object, context.getUriBuilder() ) );
      converted.add( mapping.getJaxbObjectStub( object, context ) );
    }

    return converted;
  }

  @NotNull
  public List<J> getJaxbObjects( @NotNull Iterable<? extends T> objects, @NotNull UriContext context ) {
    List<J> currentJaxbObjects = new ArrayList<J>();
    for ( T object : objects ) {
      currentJaxbObjects.add( getJaxbObject( object, context ) );
    }
    return currentJaxbObjects;
  }

  @NotNull
  public List<S> getJaxbStubs( @NotNull Iterable<? extends T> objects, @NotNull UriContext context ) {
    List<S> currentJaxbObjects = new ArrayList<S>();
    for ( T object : objects ) {
      currentJaxbObjects.add( getJaxbObjectStub( object, context ) );
    }
    return currentJaxbObjects;
  }

  /**
   * Creates the local context.
   * The local context has an updated URI context.
   *
   * @param context    the base context (not local!)
   * @param jaxbObject the jaxb object
   * @return the local context (with updated URI builder)
   */
  @NotNull
  protected UriContext createLocalContext( @NotNull UriContext context, @NotNull JaxbObject jaxbObject ) {
    assert jaxbObject.getId() != null;
    return context.create( getUri( jaxbObject, context ) );
  }

  /**
   * Creates the jaxb object
   *
   * @param object  the object
   * @param context the context
   * @return the jaxb object
   */
  @NotNull
  protected final J createJaxbObject( @NotNull T object, @NotNull UriContext context ) {
    J jaxbObject = createJaxbObject( object );

    UriContext localContext = createLocalContext( context, jaxbObject );
    setHref( jaxbObject, localContext );

    copyFieldsToJaxbObject( object, jaxbObject, localContext );
    return jaxbObject;
  }

  /**
   * Creates the JaxbObjectStub.
   * <p/>
   * A stub only contains the very basic informations off the object. It is used in collections and when referenced
   * from other objects.
   * <p/>
   * It has to contain a href to fetch the missing details later.
   *
   * @param object  the object
   * @param context the context
   * @return the stub
   */
  protected final S createJaxbObjectStub( @NotNull T object, @NotNull UriContext context ) {
    S jaxbStub = createJaxbStub( object );

    UriContext localContext = createLocalContext( context, jaxbStub );
    setHref( jaxbStub, localContext );

    copyFieldsToStub( object, jaxbStub, localContext );
    return jaxbStub;

  }

  /**
   * Sets the URI to the jaxb object.
   * Override this method for custom behaviour (e.g. no href set)
   *
   * @param jaxbObject the jaxb object
   * @param context    the context
   */
  protected void setHref( @NotNull JaxbObject jaxbObject, @NotNull UriContext context ) {
    jaxbObject.setHref( context.getUri() );
  }

  /**
   * Returns the local URI for the object
   *
   * @param object  the object (may be used to fetch the ID)
   * @param context the context
   * @return the updated uri builder
   */
  @NotNull
  protected abstract UriBuilder getUri( @NotNull JaxbObject object, @NotNull UriContext context );

  /**
   * Creates the basic JaxbObject.
   * This method also *must* set the ID
   *
   * @param object the object
   * @return the jaxb object (with the ID set)
   */
  @NotNull
  protected abstract J createJaxbObject( @NotNull T object );

  /**
   * Creates the basic stub.
   * This method also *must* set the ID
   *
   * @param object the object
   * @return the stub object (with the ID set)
   */
  protected abstract S createJaxbStub( @NotNull T object );

  /**
   * Copy the fields from the source to the target
   *
   * @param source  the source object
   * @param target  the target jaxb object
   * @param context the context
   */
  protected abstract void copyFieldsToJaxbObject( @NotNull T source, @NotNull J target, @NotNull UriContext context );

  protected abstract void copyFieldsToStub( @NotNull T source, @NotNull S target, @NotNull UriContext context );
}
