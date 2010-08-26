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

import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.AbstractMappedJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.sample.Group;
import com.cedarsoft.rest.sample.User;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.theories.*;

public class UserMappingTest extends AbstractMappedJaxbTest<User, com.cedarsoft.rest.sample.jaxb.User.Jaxb, com.cedarsoft.rest.sample.jaxb.User.Stub> {
  public UserMappingTest() {
    super( com.cedarsoft.rest.sample.jaxb.User.Jaxb.class, com.cedarsoft.rest.sample.jaxb.User.Stub.class );
  }

  @NotNull
  @Override
  protected JaxbMapping<User, com.cedarsoft.rest.sample.jaxb.User.Jaxb, com.cedarsoft.rest.sample.jaxb.User.Stub> createMapping() {
    return new UserMapping( new GroupMapping(), new DetailMapping() );
  }

  @DataPoint
  public static Entry<? extends User> dataPoint1() {
    User object = new User( "email", "name", new Group( "NOBODY", "A nobody group" ) );
    return AbstractJaxbTest.create( object, UserMappingTest.class.getResource( "UserMappingTest.dataPoint1.xml" ), UserMappingTest.class.getResource( "UserMappingTest.stub.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User> entry1() {
    User user = new User( "info@cedarsoft.com", "Johannes Schneider" );
    user.addFriend( new User( "markus@mustermann.de", "Markus Mustermann" ) );
    user.addFriend( new User( "markus2@mustermann.de", "Markus2 Mustermann" ) );
    user.addFriend( new User( "markus3@mustermann.de", "Markus3 Mustermann" ) );

    user.addDetail( new com.cedarsoft.rest.sample.Detail( "1", "the first detail" ) );
    user.addDetail( new com.cedarsoft.rest.sample.Detail( "2", "the second detail" ) );

    return create( user, UserJaxbTest.class.getResource( "UserMappingTest.recursive.xml" ), UserJaxbTest.class.getResource( "UserMappingTest.recursive.stub.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User> recursive() {
    User user = new User( "info@cedarsoft.com", "Johannes Schneider" );
    User user1 = new User( "markus@mustermann.de", "Markus Mustermann" );
    user.addFriend( user1 );
    user.addFriend( new User( "markus2@mustermann.de", "Markus2 Mustermann" ) );
    user.addFriend( new User( "markus3@mustermann.de", "Markus3 Mustermann" ) );

    user.addDetail( new com.cedarsoft.rest.sample.Detail( "1", "the first detail" ) );
    user.addDetail( new com.cedarsoft.rest.sample.Detail( "2", "the second detail" ) );
    
    user1.addFriend( user );

    return create( user, UserJaxbTest.class.getResource( "UserMappingTest.recursive.xml" ), UserJaxbTest.class.getResource( "UserMappingTest.recursive.stub.xml" ) );
  }
}
