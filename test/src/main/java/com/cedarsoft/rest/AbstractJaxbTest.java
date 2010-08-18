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
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractJaxbTest<J extends JaxbObject> {
  protected JAXBContext context;

  @NotNull
  protected abstract Class<J> getJaxbType();

  @Before
  public void setup() throws JAXBException {
    context = createContext();
    assertNotNull( context );
  }


  @Test
  public void testNameSpace() throws Exception {
    String namespace = getJaxbType().getAnnotation( XmlRootElement.class ).namespace();
    assertNotNull( namespace );
    Assert.assertFalse( "Missing namespace for <" + getJaxbType().getName() + ">", namespace.equals( "##default" ) );
    Assert.assertTrue( namespace.length() > 0 );
  }

  @NotNull
  public JAXBContext getContext() {
    return context;
  }

  @NotNull
  protected JAXBContext createContext() throws JAXBException {
    List<? extends Class<?>> classesToBeBound = getClassesToBeBound();
    return JAXBContext.newInstance( classesToBeBound.toArray( new Class[new ArrayList<Class<?>>( classesToBeBound ).size()] ) );
  }

  @NotNull
  protected List<? extends Class<?>> getClassesToBeBound() {
    List<Class<?>> list = new ArrayList<Class<?>>();
    list.add( getJaxbType() );
    Collection<? extends Class<?>> additionalClassesToBeBound = getAdditionalClassesToBeBound();
    if ( additionalClassesToBeBound != null ) {
      list.addAll( additionalClassesToBeBound );
    }
    return list;
  }

  @Nullable
  protected Collection<? extends Class<?>> getAdditionalClassesToBeBound() {
    return null;
  }

  @NotNull
  @NonNls
  protected String getXmlName() {
    String className = getClass().getSimpleName();
    return className + ".xml";
  }

  @NotNull
  protected Marshaller createMarshaller() throws JAXBException {
    assertNotNull( context );
    return context.createMarshaller();
  }

  @NotNull
  protected Unmarshaller createUnmarshaller() throws JAXBException {
    return context.createUnmarshaller();
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls byte[] expected ) {
    return new Entry<T>( object, expected );
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls String expected ) {
    return new Entry<T>( object, expected.getBytes() );
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls URL expected ) {
    try {
      return new Entry<T>( object, IOUtils.toByteArray( expected.openStream() ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  @NotNull
  protected static <T> Entry<? extends T> create( @NotNull T object, @NotNull @NonNls InputStream expected ) {
    try {
      return new Entry<T>( object, IOUtils.toByteArray( expected ) );
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
