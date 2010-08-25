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

import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.JaxbMappingContext;
import com.google.inject.Inject;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UserMapping extends JaxbMapping<com.cedarsoft.rest.sample.User, User.Jaxb, User.Stub> {
  @NonNls
  @NotNull
  public static final String PATH_USERS = "users";
  @NonNls
  public static final String PATH_ID = "{id}";

  @Inject
  public UserMapping( @NotNull GroupMapping groupMapping ) {
    getDelegatesMapping().addMapping( User.Jaxb.class, User.Stub.class, this );
    getDelegatesMapping().addMapping( Group.Jaxb.class, Group.Stub.class, groupMapping );
  }

  @Override
  protected void setUris( @NotNull JaxbObject object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
    object.setHref( uriBuilder.path( PATH_USERS ).path( PATH_ID ).build( object.getId() ) );
  }

  @NotNull
  @Override
  protected User.Jaxb createJaxbObject( @NotNull com.cedarsoft.rest.sample.User object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    User.Jaxb jaxbObject = new User.Jaxb();
    jaxbObject.setId( object.getEmail() );
    jaxbObject.setEmail( object.getEmail() );
    jaxbObject.setName( object.getName() );
    jaxbObject.setFriends( getStub( User.Stub.class, object.getFriends(), context ) );
    jaxbObject.setGroup( getStub( Group.Stub.class, object.getGroup(), context ) );
    return jaxbObject;
  }

  @Override
  protected User.Stub createJaxbObjectStub( @NotNull com.cedarsoft.rest.sample.User object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    User.Stub jaxbObject = new User.Stub();
    jaxbObject.setId( object.getEmail() );
    jaxbObject.setEmail( object.getEmail() );
    jaxbObject.setName( object.getName() );
    return jaxbObject;
  }
}
