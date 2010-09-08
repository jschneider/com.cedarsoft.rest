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
import com.cedarsoft.JsonUtils;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbTestUtils;
import com.cedarsoft.rest.SimpleJaxbTest;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.json.impl.JSONMarshallerImpl;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.*;
import org.junit.experimental.theories.*;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UserJaxbTest extends SimpleJaxbTest<User.Jaxb, User.Stub> {
  public UserJaxbTest() {
    super( User.Jaxb.class, User.Stub.class, User.Collection.class );
  }

  @Test
  public void testJson() throws Exception {
    User.Collection collection = new User.Collection( ImmutableList.<User.Stub>of(
      new User.Stub( "a" ),
      new User.Stub( "b" ),
      new User.Stub( "c" )
    ), 77, 300 );

    for ( User.Stub stub : collection.getUsers() ) {
      stub.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    }

    ObjectMapper mapper = new ObjectMapper();
    AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
    // make deserializer use JAXB annotations (only)
    mapper.getDeserializationConfig().setAnnotationIntrospector( introspector );
    // make serializer use JAXB annotations (only)
    mapper.getSerializationConfig().setAnnotationIntrospector( introspector );


    StringWriter out = new StringWriter();
    mapper.writeValue( out, collection );

    JsonUtils.assertJsonEquals( getClass().getResource( "UserJaxbTest.collection.json" ), out.toString() );

    User.Collection read = mapper.readValue( out.toString(), User.Collection.class );
    assertEquals( 300, read.getMaxLength() );
    assertEquals( 77, read.getStartIndex() );
    assertNotNull( read.getUsers() );
    assertEquals( 3, read.getUsers().size() );

    assertEquals( "a", read.getUsers().get( 0 ).getId() );
    assertEquals( "b", read.getUsers().get( 1 ).getId() );
    assertEquals( "c", read.getUsers().get( 2 ).getId() );
  }

  @Test
  public void testJsonSimple() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
    // make deserializer use JAXB annotations (only)
    mapper.getDeserializationConfig().setAnnotationIntrospector( introspector );
    // make serializer use JAXB annotations (only)
    mapper.getSerializationConfig().setAnnotationIntrospector( introspector );


    User.Stub object = new User.Stub( "a" );
    object.setName( "daName" );
    object.setEmail( "daMail" );
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );

    StringWriter out = new StringWriter();
    mapper.writeValue( out, object );

    JsonUtils.assertJsonEquals( "{" +
      "  \"href\" : \"http://test.running/here\"," +
      "  \"id\" : \"a\"," +
      "  \"email\" : \"daMail\"," +
      "  \"name\" : \"daName\"" +
      "}", out.toString() );

    User.Stub read = mapper.readValue( out.toString(), User.Stub.class );
    assertEquals( "http://test.running/here", read.getHref().toString() );
    assertEquals( "daName", read.getName() );
    assertEquals( "daMail", read.getEmail() );
  }

  @Test
  public void testCollection() throws Exception {
    User.Collection collection = new User.Collection( ImmutableList.<User.Stub>of(
      new User.Stub( "a" ),
      new User.Stub( "b" ),
      new User.Stub( "c" )
    ), 7, 0 );
    collection.setHref( JaxbTestUtils.createTestUriBuilder().build() );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    jaxbRule.addTypeToBeBound( User.Collection.class );

    JAXBContext context = JAXBContext.newInstance( User.Jaxb.class, User.Stub.class, User.Collection.class );
    context.createMarshaller().marshal( collection, out );

    AssertUtils.assertXMLEquals( IOUtils.toString( getClass().getResourceAsStream( "UserJaxbTest.collection.xml" ) ), out.toString() );
  }

  @Test
  public void testCollectionJsonBadger() throws Exception {
    User.Collection collection = new User.Collection( ImmutableList.<User.Stub>of(
      new User.Stub( "a" ),
      new User.Stub( "b" ),
      new User.Stub( "c" )
    ) );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    jaxbRule.addTypeToBeBound( User.Collection.class );

    JSONJAXBContext context = new JSONJAXBContext( JSONConfiguration.badgerFish().build(), User.Jaxb.class, User.Stub.class, User.Collection.class );
    JSONMarshaller marshaller = context.createJSONMarshaller();
    marshaller.marshallToJSON( collection, out );

    assertEquals( "{\"users:users\":{\"@xmlns\":{\"ci-stub\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/camera-info\\/stub\",\"user\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/user\",\"ci\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/camera-info\",\"users\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/user\\/list\",\"user-stub\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/user\\/stub\",\"email\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/email\",\"detail\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/detail\",\"cam-stub\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/camera\\/stub\",\"group-stub\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/group\\/stub\",\"group\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/group\",\"detail-stub\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/detail\\/stub\",\"email-stub\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/email\\/stub\",\"cam\":\"http:\\/\\/cedarsoft.com\\/rest\\/sample\\/camera\"},\"@startIndex\":\"0\",\"@maxLength\":\"0\",\"user-stub:user\":[{\"@id\":\"a\"},{\"@id\":\"b\"},{\"@id\":\"c\"}]}}", out.toString() );
  }

  @Test
  public void testCollectionJsonDefault() throws Exception {
    User.Collection collection = new User.Collection( ImmutableList.<User.Stub>of(
      new User.Stub( "a" ),
      new User.Stub( "b" ),
      new User.Stub( "c" )
    ) );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    jaxbRule.addTypeToBeBound( User.Collection.class );

    JSONJAXBContext context = new JSONJAXBContext( JSONConfiguration.DEFAULT, User.Jaxb.class, User.Stub.class, User.Collection.class );
    JSONMarshaller marshaller = context.createJSONMarshaller();
    assertEquals( JSONMarshallerImpl.class, marshaller.getClass() );
    marshaller.marshallToJSON( collection, out );

    assertEquals( "{\"@startIndex\":\"0\",\"@maxLength\":\"0\",\"user\":[{\"@id\":\"a\"},{\"@id\":\"b\"},{\"@id\":\"c\"}]}", out.toString() );
  }

  @Test
  public void testCollectionJsonNatural() throws Exception {
    User.Collection collection = new User.Collection( ImmutableList.<User.Stub>of(
      new User.Stub( "a" ),
      new User.Stub( "b" ),
      new User.Stub( "c" )
    ) );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    jaxbRule.addTypeToBeBound( User.Collection.class );

    JSONJAXBContext context = new JSONJAXBContext( JSONConfiguration.natural().build(), User.Jaxb.class, User.Stub.class, User.Collection.class );
    JSONMarshaller marshaller = context.createJSONMarshaller();
    assertEquals( JSONMarshallerImpl.class, marshaller.getClass() );
    marshaller.marshallToJSON( collection, out );

    assertEquals( "{\"startIndex\":0,\"maxLength\":0,\"user\":[{\"id\":\"a\"},{\"id\":\"b\"},{\"id\":\"c\"}]}", out.toString() );

    User.Collection unmarshalled = context.createJSONUnmarshaller().unmarshalFromJSON( new ByteArrayInputStream( out.toByteArray() ), User.Collection.class );
    assertEquals( 3, unmarshalled.getUsers().size() );
  }

  @DataPoint
  public static Entry<? extends User.Jaxb> dataPoint1() {
    User.Jaxb object = new User.Jaxb( "daId" );
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    Group.Stub group = new Group.Stub( "groupId" );
    object.setGroup( group );
    object.setEmail( "email" );
    object.setName( "name" );

    User.Stub friend = new User.Stub();
    friend.setName( "Markus Mustermann" );
    friend.setEmail( "markus@mustermann.com" );

    User.Stub friend2 = new User.Stub();
    friend2.setName( "Eva Mustermann" );
    friend2.setEmail( "eva@mustermann.com" );

    object.setFriends( Arrays.asList( friend, friend2 ) );

    Detail.Stub detail = new Detail.Stub( "1" );
    detail.setText( "a detail text..." );
    Detail.Stub detail1 = new Detail.Stub( "2" );
    detail1.setText( "Another detail with a long text" );
    object.setDetails( Arrays.asList( detail, detail1 ) );

    return create( object, UserJaxbTest.class.getResource( "UserJaxbTest.dataPoint1.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User.Jaxb> notFriends() {
    User.Jaxb object = new User.Jaxb( "daId" );
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setEmail( "email" );
    object.setName( "name" );

    Group.Stub group = new Group.Stub( "groupId" );
    object.setGroup( group );

    Detail.Stub detail = new Detail.Stub( "1" );
    detail.setText( "a detail text..." );
    Detail.Stub detail1 = new Detail.Stub( "2" );
    detail1.setText( "Another detail with a long text" );
    object.setDetails( Arrays.asList( detail, detail1 ) );

    return create( object, UserJaxbTest.class.getResource( "UserJaxbTest.noFriends.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User.Stub> stub() {
    User.Stub object = new User.Stub( "daId" );
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setEmail( "email" );
    object.setName( "name" );

    return create( object, UserJaxbTest.class.getResource( "UserJaxbTest.stub.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User.Collection> smallCollection() {
    User.Collection object = new User.Collection( Arrays.asList( new User.Stub( "a" ), new User.Stub( "b" ), new User.Stub( "c" ) ), 7, 0 );
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    return create( object, UserJaxbTest.class.getResource( "UserJaxbTest.collection.xml" ) );
  }
}
