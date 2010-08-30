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


package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.AssertUtils;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbTestUtils;
import com.cedarsoft.rest.SimpleJaxbTest;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.experimental.theories.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class UserJaxbTest extends SimpleJaxbTest<User.Jaxb, User.Stub> {
  public UserJaxbTest() {
    super( User.Jaxb.class, User.Stub.class );
  }

  @Test
  public void testCollection() throws Exception {
    User.Collection collection = new User.Collection( ImmutableList.<User.Stub>of(
      new User.Stub( "a" ),
      new User.Stub( "b" ),
      new User.Stub( "c" )
    ) );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    jaxbRule.addTypeToBound( User.Collection.class );

    createMarshaller().marshal( collection, out );

    AssertUtils.assertXMLEquals( IOUtils.toString( getClass().getResourceAsStream( "UserJaxbTest.collection.xml" ) ), out.toString() );
  }

  @DataPoint
  public static Entry<? extends User.Jaxb> dataPoint1() {
    User.Jaxb object = new User.Jaxb();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    Group.Stub group = new Group.Stub();
    group.setId( "groupId" );
    object.setGroup( group );
    object.setId( "daId" );
    object.setEmail( "email" );
    object.setName( "name" );

    User.Stub friend = new User.Stub();
    friend.setName( "Markus Mustermann" );
    friend.setEmail( "markus@mustermann.com" );

    User.Stub friend2 = new User.Stub();
    friend2.setName( "Eva Mustermann" );
    friend2.setEmail( "eva@mustermann.com" );

    object.setFriends( Arrays.asList( friend, friend2 ) );

    Detail.Stub detail = new Detail.Stub();
    detail.setId( "1" );
    detail.setText( "a detail text..." );
    Detail.Stub detail1 = new Detail.Stub();
    detail1.setId( "2" );
    detail1.setText( "Another detail with a long text" );
    object.setDetails( Arrays.asList( detail, detail1 ) );

    return create( object, UserJaxbTest.class.getResource( "UserJaxbTest.dataPoint1.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User.Jaxb> notFriends() {
    User.Jaxb object = new User.Jaxb();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setId( "daId" );
    object.setEmail( "email" );
    object.setName( "name" );

    Group.Stub group = new Group.Stub();
    group.setId( "groupId" );
    object.setGroup( group );

    Detail.Stub detail = new Detail.Stub();
    detail.setId( "1" );
    detail.setText( "a detail text..." );
    Detail.Stub detail1 = new Detail.Stub();
    detail1.setText( "Another detail with a long text" );
    detail1.setId( "2" );
    object.setDetails( Arrays.asList( detail, detail1 ) );

    return create( object, UserJaxbTest.class.getResource( "UserJaxbTest.noFriends.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User.Stub> stub() {
    User.Stub object = new User.Stub();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setId( "daId" );
    object.setEmail( "email" );
    object.setName( "name" );

    return create( object, UserJaxbTest.class.getResource( "UserJaxbTest.stub.xml" ) );
  }
}
