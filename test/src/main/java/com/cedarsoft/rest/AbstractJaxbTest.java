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
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
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
public abstract class AbstractJaxbTest<J extends JaxbObject, S extends JaxbStub> {
  @Rule
  public JaxbRule jaxbRule = new JaxbRule( getJaxbType(), getJaxbStubType() );

  @NotNull
  private final Class<J> jaxbType;
  @NotNull
  private final Class<S> jaxbStubType;

  protected AbstractJaxbTest( @NotNull Class<J> jaxbType, @NotNull Class<S> jaxbStubType ) {
    this.jaxbType = jaxbType;
    this.jaxbStubType = jaxbStubType;
  }

  @NotNull
  protected final Class<J> getJaxbType() {
    return jaxbType;
  }

  @NotNull
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

  @NotNull
  public Marshaller createMarshaller() throws JAXBException {
    return jaxbRule.createMarshaller();
  }

  @NotNull
  public Unmarshaller createUnmarshaller() throws JAXBException {
    return jaxbRule.createUnmarshaller();
  }

  @NotNull
  @NonNls
  protected String getXmlName() {
    String className = getClass().getSimpleName();
    return className + ".xml";
  }

  protected void verifyDeserialized( @NotNull J deserialized, @NotNull J originalJaxbObject ) throws IllegalAccessException {
    assertEquals( originalJaxbObject, deserialized );
  }

  protected void verifyDeserializedStub( @NotNull S deserialized, @NotNull S originalJaxbStub ) throws IllegalAccessException {
    assertEquals( originalJaxbStub, deserialized );
  }

  protected boolean isJaxbObjectType( Entry<?> entry ) {
    return getJaxbType().equals( entry.getObject().getClass() );
  }

  protected boolean isJaxbStubType( Entry<?> entry ) {
    Class<?> objectType = entry.getObject().getClass();
    return getJaxbStubType().equals( objectType );
  }

  @Deprecated
  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls byte[] expected ) {
    return create( object, expected, expected );
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls byte[] expected, @NotNull @NonNls byte[] stubExpected ) {
    return new Entry<T>( object, expected, stubExpected );
  }

  @Deprecated
  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls String expected ) {
    return create( object, expected, expected );
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls String expected, @NotNull @NonNls String stubExpected ) {
    return new Entry<T>( object, expected.getBytes(), stubExpected.getBytes() );
  }

  @Deprecated
  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls URL expected ) {
    return create( object, expected, expected );
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls URL expected, @NotNull @NonNls URL stubExpected ) {
    try {
      return new Entry<T>( object, IOUtils.toByteArray( expected.openStream() ), IOUtils.toByteArray( stubExpected.openStream() ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @Deprecated
  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls InputStream expected ) {
    return create( object, expected, expected );
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls InputStream expected, @NotNull @NonNls InputStream stubExpected ) {
    try {
      return new Entry<T>( object, IOUtils.toByteArray( expected ), IOUtils.toByteArray( stubExpected ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
