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
import com.google.common.collect.Lists;
import com.sun.jersey.api.uri.UriBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.*;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class JaxbMappingTest {
  private JaxbMapping<MyObject, MyJaxbObject> mapping;

  @Before
  public void setUp() throws Exception {
    mapping = new JaxbMapping<MyObject, MyJaxbObject>() {
      @Override
      protected void setUris( @NotNull MyJaxbObject object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
      }

      @NotNull
      @Override
      protected MyJaxbObject createJaxbObject( @NotNull MyObject object, @NotNull JaxbMappingContext context ) {
        MyJaxbObject jaxbObject = new MyJaxbObject();
        jaxbObject.setDaInt( object.daInt );
        return jaxbObject;
      }
    };
  }

  @Test
  public void testGetJaxbObject() throws Exception {
    MyObject myObject = new MyObject( 24 );

    MyJaxbObject myJaxbObject = mapping.getJaxbObject( myObject, new UriBuilderImpl() );
    assertSame( myObject.daInt, myJaxbObject.daInt );

    //test cache
    assertSame( myJaxbObject, mapping.getJaxbObject( myObject, new UriBuilderImpl() ) );
  }

  @Test
  public void testGetJaxbObjectNull() throws Exception {
    MyObject myObject = new MyObject( 42 );

    MyJaxbObject myJaxbObject = mapping.getJaxbObject( myObject, null );
    assertSame( myObject.daInt, myJaxbObject.daInt );

    //test cache
    assertSame( myJaxbObject, mapping.getJaxbObject( myObject, null ) );
  }

  @Test
  public void testGetJaxbObject2() throws Exception {
    MyObject myObject1 = new MyObject(7);
    MyObject myObject2 = new MyObject(8);

    List<MyJaxbObject> myJaxbObjects = mapping.getJaxbObjects( Lists.newArrayList( myObject1, myObject2 ), new UriBuilderImpl() );
    assertSame( myObject1.daInt, myJaxbObjects.get( 0 ).daInt );
    assertSame( myObject2.daInt, myJaxbObjects.get( 1 ).daInt );

    //test cache
    assertSame( myJaxbObjects.get( 0 ), mapping.getJaxbObject( myObject1, new UriBuilderImpl() ) );
    assertSame( myJaxbObjects.get( 1 ), mapping.getJaxbObject( myObject2, new UriBuilderImpl() ) );
  }

  protected static class MyObject {
    private final int daInt;

    MyObject( int daInt ) {
      this.daInt = daInt;
    }
  }

  protected static class MyJaxbObject extends AbstractJaxbObject {
    private int daInt;

    public int getDaInt() {
      return daInt;
    }

    public void setDaInt( int daInt ) {
      this.daInt = daInt;
    }
  }
}
