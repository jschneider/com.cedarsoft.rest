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

package com.cedarsoft.rest.server;

import javax.annotation.Nonnull;
import org.junit.*;

import javax.ws.rs.core.UriBuilder;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UriContextTest {
  private UriContext context;

  @Before
  public void setUp() throws Exception {
    context = new UriContext( createTestUriBuilder(), createTestUriBuilder() );
  }

  @Test
  public void testIT() {
    assertEquals( "http://test.running/here/dir", context.getUriBuilder().path( "dir" ).build().toString() );
    assertEquals( "http://test.running/here/dir2", context.getUriBuilder().path( "dir2" ).build().toString() );
    assertEquals( "http://test.running/here/dir3", context.getUriBuilder().path( "dir3" ).build().toString() );
  }

  @Test
  public void testSubApi() throws Exception {
    UriBuilder uriBuilder = context.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );

    UriContext newContext = context.create( uriBuilder );
    assertEquals( "http://test.running/here/dir/sub", newContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testSubSub() throws Exception {
    assertEquals( "http://test.running/here", context.getUri().toString() );

    //Within users
    UriContext contextUser = context.create( context.getUriBuilder().path( "users" ).path( "info@cedarsoft.com" ) );
    assertEquals( "http://test.running/here/users/info@cedarsoft.com", contextUser.getUri().toString() );

    //Within user details
    UriContext contextDetail = contextUser.create( contextUser.getUriBuilder().path( "details" ).path( "1" ) );
    assertEquals( "http://test.running/here/users/info@cedarsoft.com/details/1", contextDetail.getUri().toString() );

    //Now the group...
    UriContext contextGroup = contextUser.create( contextUser.getBaseUriBuilder().path( "groups" ).path( "nobody" ) );
    assertEquals( "http://test.running/here/groups/nobody", contextGroup.getUri().toString() );
  }

  @Test
  public void testSubClone() throws Exception {
    UriBuilder uriBuilder = context.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );
    UriContext newContext = context.create( uriBuilder );

    assertEquals( "http://test.running/here/dir/another", uriBuilder.path( "another" ).build().toString() );

    assertEquals( "http://test.running/here/dir/sub", newContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testUriBuilder() throws Exception {
    UriBuilder uriBuilder = context.getUriBuilder().path( "users" ).path( "{id}" );
    assertEquals( "http://test.running/here/users/info@cedarsoft.com", uriBuilder.build( "info@cedarsoft.com" ).toString() );

    UriContext newContext = context.create( UriBuilder.fromUri( uriBuilder.build( "info@cedarsoft.com" ) ) );

    assertEquals( "http://test.running/here/users/info@cedarsoft.com/details/7", newContext.getUriBuilder().path( "details" ).path( "{id}" ).build( "7" ).toString() );
  }

  @Nonnull
  public static UriBuilder createTestUriBuilder() {
    return UriBuilder.fromUri( "http://test.running/here" );
  }

  @Nonnull
  public static UriContext createTestUriContext() {
    return new UriContext( createTestUriBuilder(), createTestUriBuilder() );
  }
}
