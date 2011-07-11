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

package com.cedarsoft.rest.test;

import com.cedarsoft.rest.model.AbstractJaxbCollection;
import com.cedarsoft.rest.model.JaxbCollection;
import com.cedarsoft.rest.model.JaxbObject;
import com.cedarsoft.rest.model.JaxbStub;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.junit.*;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractJaxbTest<J extends JaxbObject, S extends JaxbStub<J>> {
  @Rule
  public JaxbRule jaxbRule;

  @Nonnull
  private final Class<J> jaxbType;
  @Nonnull
  private final Class<S> jaxbStubType;
  @Nullable
  private final Class<? extends JaxbCollection> jaxbCollectionType;

  protected AbstractJaxbTest( @Nonnull Class<J> jaxbType, @Nonnull Class<S> jaxbStubType ) {
    this( jaxbType, jaxbStubType, null );
  }

  protected AbstractJaxbTest( @Nonnull Class<J> jaxbType, @Nonnull Class<S> jaxbStubType, @Nullable Class<? extends JaxbCollection> jaxbCollectionType ) {
    this.jaxbType = jaxbType;
    this.jaxbStubType = jaxbStubType;
    this.jaxbCollectionType = jaxbCollectionType;

    jaxbRule = new JaxbRule( getJaxbType(), getJaxbStubType(), jaxbCollectionType );
  }

  @Nonnull
  public Class<? extends JaxbCollection> getJaxbCollectionType() {
    if ( jaxbCollectionType == null ) {
      throw new IllegalStateException( "No jaxbCollectionType set" );
    }
    return jaxbCollectionType;
  }

  @Nonnull
  protected final Class<J> getJaxbType() {
    return jaxbType;
  }

  @Nonnull
  protected final Class<S> getJaxbStubType() {
    return jaxbStubType;
  }

  @Test
  public void testNameSpace() throws Exception {
    String namespace = getJaxbType().getAnnotation( XmlRootElement.class ).namespace();
    assertNotNull( namespace );
    Assert.assertFalse( "Missing namespace for <" + getJaxbType().getName() + ">", namespace.equals( "##default" ) );
    Assert.assertTrue( namespace.length() > 0 );
  }

  @Nonnull
  public Marshaller createMarshaller() throws JAXBException {
    return jaxbRule.createMarshaller();
  }

  @Nonnull
  public Unmarshaller createUnmarshaller() throws JAXBException {
    return jaxbRule.createUnmarshaller();
  }

  @Nonnull

  protected String getXmlName() {
    String className = getClass().getSimpleName();
    return className + ".xml";
  }

  protected void verifyDeserialized( @Nonnull JaxbCollection deserialized, @Nonnull JaxbCollection originalCollection ) {
    assertEquals( originalCollection, deserialized );
    EqualsBuilder.reflectionEquals( deserialized, originalCollection );
  }

  protected void verifyDeserialized( @Nonnull J deserialized, @Nonnull J originalJaxbObject ) throws IllegalAccessException {
    assertEquals( originalJaxbObject, deserialized );
    EqualsBuilder.reflectionEquals( deserialized, originalJaxbObject );
  }

  protected void verifyDeserializedStub( @Nonnull S deserialized, @Nonnull S originalJaxbStub ) throws IllegalAccessException {
    assertEquals( originalJaxbStub, deserialized );
    EqualsBuilder.reflectionEquals( deserialized, originalJaxbStub );
  }

  protected boolean isJaxbObjectType( @Nonnull Entry<?> entry ) {
    return getJaxbType().equals( entry.getObject().getClass() );
  }

  protected boolean isJaxbCollectionObjectType( @Nonnull Entry<?> entry ) {
    return AbstractJaxbCollection.class.isAssignableFrom( entry.getObject().getClass() );
  }

  protected boolean isJaxbStubType( @Nonnull Entry<?> entry ) {
    Class<?> objectType = entry.getObject().getClass();
    return getJaxbStubType().equals( objectType );
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  byte[] expected ) {
    return create( object, expected, expected );
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  byte[] expected, @Nonnull  byte[] stubExpected ) {
    return new Entry<T>( object, expected, stubExpected );
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  String expected ) {
    return create( object, expected, expected );
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  String expected, @Nonnull  String stubExpected ) {
    return new Entry<T>( object, expected.getBytes(), stubExpected.getBytes() );
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  URL expected ) {
    return create( object, expected, expected );
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  URL expected, @Nonnull  URL stubExpected ) {
    try {
      return new Entry<T>( object, IOUtils.toByteArray( expected.openStream() ), IOUtils.toByteArray( stubExpected.openStream() ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  InputStream expected ) {
    return create( object, expected, expected );
  }

  @Nonnull
  protected static <T> Entry<? extends T> create( @Nonnull T object, @Nonnull  InputStream expected, @Nonnull  InputStream stubExpected ) {
    try {
      return new Entry<T>( object, IOUtils.toByteArray( expected ), IOUtils.toByteArray( stubExpected ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
