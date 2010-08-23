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

import com.cedarsoft.rest.AbstractMappedJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbMapping;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.theories.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UserJaxbMappingTest extends AbstractMappedJaxbTest<com.cedarsoft.rest.sample.User, User.Jaxb, User.Stub> {
  public UserJaxbMappingTest() {
    super( User.Jaxb.class, User.Stub.class );
  }

  @NotNull
  @Override
  protected JaxbMapping<com.cedarsoft.rest.sample.User, User.Jaxb, User.Stub> createMapping() {
    return new UserMapping();
  }

  @DataPoint
  public static Entry<? extends com.cedarsoft.rest.sample.User> entry1() {
    com.cedarsoft.rest.sample.User user = new com.cedarsoft.rest.sample.User( "info@cedarsoft.com", "Johannes Schneider" );
    user.addFriend( new com.cedarsoft.rest.sample.User( "markus@mustermann.de", "Markus Mustermann" ) );
    user.addFriend( new com.cedarsoft.rest.sample.User( "markus2@mustermann.de", "Markus2 Mustermann" ) );
    user.addFriend( new com.cedarsoft.rest.sample.User( "markus3@mustermann.de", "Markus3 Mustermann" ) );

    return create( user, UserJaxbTest.class.getResource( "UserJaxbMappingTest.xml" ), UserJaxbTest.class.getResource( "UserJaxbMappingTest.stub.xml" ) );
  }

  @DataPoint
  public static Entry<? extends com.cedarsoft.rest.sample.User> recursive() {
    com.cedarsoft.rest.sample.User user = new com.cedarsoft.rest.sample.User( "info@cedarsoft.com", "Johannes Schneider" );
    com.cedarsoft.rest.sample.User user1 = new com.cedarsoft.rest.sample.User( "markus@mustermann.de", "Markus Mustermann" );
    user.addFriend( user1 );
    user.addFriend( new com.cedarsoft.rest.sample.User( "markus2@mustermann.de", "Markus2 Mustermann" ) );
    user.addFriend( new com.cedarsoft.rest.sample.User( "markus3@mustermann.de", "Markus3 Mustermann" ) );

    user1.addFriend( user );

    return create( user, UserJaxbTest.class.getResource( "UserJaxbMappingTest.xml" ), UserJaxbTest.class.getResource( "UserJaxbMappingTest.stub.xml" ) );
  }
}
