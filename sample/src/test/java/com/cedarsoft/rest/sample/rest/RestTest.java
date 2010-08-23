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

package com.cedarsoft.rest.sample.rest;

import com.cedarsoft.rest.sample.jaxb.User;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.cedarsoft.AssertUtils.assertXMLEquals;
import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RestTest extends JerseyTest {
  /**
   * @noinspection RefusedBequest
   */
  @Override
  protected AppDescriptor configure() {
    return new WebAppDescriptor.Builder()
      .contextListenerClass( GuiceConfig.class )
      .filterClass( GuiceFilter.class )
      .servletPath( "/" )
      .build();
  }

  @Test
  public void testTestUser() throws Exception {
    assertXMLEquals( getClass().getResource( "Rest.testUser.xml" ), resource().path( "users/test" ).accept( MediaType.APPLICATION_XML ).get( String.class ) );
    assertXMLEquals( getClass().getResource( "Rest.testUser.xml" ), resource().path( "users/test" ).accept( MediaType.TEXT_XML ).get( String.class ) );

    User.Jaxb testUser = resource().path( "users/test" ).accept( MediaType.APPLICATION_XML ).get( User.Jaxb.class );
    assertNotNull( testUser );
    assertEquals( "test@test.com", testUser.getEmail() );
    assertEquals( 1, testUser.getFriends().size() );
    assertEquals( "Test User", testUser.getName() );
    assertEquals( "http://localhost:9998/users/test@test.com", testUser.getHref().toString() );
    assertEquals( "test@test.com", testUser.getId() );
  }

  @Test
  public void testXml() throws Exception {
    assertXMLEquals( getClass().getResource( "Rest.users.xml" ), resource().path( "users" ).type( MediaType.APPLICATION_XML ).get( String.class ) );
    assertXMLEquals( getClass().getResource( "Rest.testUser.xml" ), resource().path( "users/test" ).type( MediaType.APPLICATION_XML ).get( String.class ) );
  }

  @Test
  public void testGetUsers() throws Exception {
    List<User.Stub> users = resource().path( "users" ).type( MediaType.APPLICATION_XML ).get( new GenericType<List<User.Stub>>() {
    } );
    assertNotNull( users );

    assertEquals( 3, users.size() );

    assertEquals( "Johannes Schneider", users.get( 0 ).getName() );
    assertEquals( "info@cedarsoft.de", users.get( 0 ).getEmail() );
    assertEquals( "http://localhost:9998/users/info@cedarsoft.de", users.get( 0 ).getHref().toString() );

    assertEquals( "Markus Mustermann", users.get( 1 ).getName() );
    assertEquals( "markus@mustermann.de", users.get( 1 ).getEmail() );
    assertEquals( "http://localhost:9998/users/markus@mustermann.de", users.get( 1 ).getHref().toString() );

    assertEquals( "Eva Mustermann", users.get( 2 ).getName() );
    assertEquals( "eva@mustermann.de", users.get( 2 ).getEmail() );
    assertEquals( "http://localhost:9998/users/eva@mustermann.de", users.get( 2 ).getHref().toString() );
  }

  @Test
  public void testFetchFurther() throws Exception {
    List<User.Stub> users = resource().path( "users" ).type( MediaType.APPLICATION_XML ).get( new GenericType<List<User.Stub>>() {
    } );

    {
      User.Stub stub = users.get( 0 );
      User.Jaxb user = client().resource( stub.getHref() ).type( MediaType.APPLICATION_XML ).get( User.Jaxb.class );
      assertEquals( "Johannes Schneider", user.getName() );
      assertEquals( "Johannes Schneider", stub.getName() );
      assertEquals( 2, user.getFriends().size() );
      assertEquals( "Markus Mustermann", user.getFriends().get( 0 ).getName() );
      assertEquals( "Eva Mustermann", user.getFriends().get( 1 ).getName() );
    }

    {
      User.Stub stub = users.get( 1 );
      User.Jaxb user = client().resource( stub.getHref() ).type( MediaType.APPLICATION_XML ).get( User.Jaxb.class );
      assertEquals( "Markus Mustermann", user.getName() );
      assertEquals( "Markus Mustermann", stub.getName() );
      assertEquals( 2, user.getFriends().size() );
      assertEquals( "Eva Mustermann", user.getFriends().get( 0 ).getName() );
      assertEquals( "Johannes Schneider", user.getFriends().get( 1 ).getName() );
    }
    
    {
      User.Stub stub = users.get( 2 );
      User.Jaxb user = client().resource( stub.getHref() ).type( MediaType.APPLICATION_XML ).get( User.Jaxb.class );
      assertEquals( "Eva Mustermann", user.getName() );
      assertEquals( "Eva Mustermann", stub.getName() );
      assertEquals( 1, user.getFriends().size() );
      assertEquals( "Markus Mustermann", user.getFriends().get( 0 ).getName() );
    }
  }

  @Test
  public void testJson() throws Exception {
    assertEquals( IOUtils.toString( getClass().getResourceAsStream( "Rest.testUser.json" ) ), resource().path( "users/test" ).accept( MediaType.APPLICATION_JSON ).get( String.class ) );
  }
}
