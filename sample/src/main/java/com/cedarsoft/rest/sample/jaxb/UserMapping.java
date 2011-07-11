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
import com.cedarsoft.rest.UriContext;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

import javax.ws.rs.core.UriBuilder;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UserMapping extends JaxbMapping<com.cedarsoft.rest.sample.User, User.Jaxb, User.Stub> {

  @Nonnull
  public static final String PATH_USERS = "users";

  @Inject
  public UserMapping( @Nonnull GroupMapping groupMapping, @Nonnull DetailMapping detailMapping ) {
    getDelegatesMapping().addMapping( User.Jaxb.class, User.Stub.class, this );
    getDelegatesMapping().addMapping( Group.Jaxb.class, Group.Stub.class, groupMapping );
    getDelegatesMapping().addMapping( Detail.Jaxb.class, Detail.Stub.class, detailMapping );
  }

  @Nonnull
  @Override
  protected UriBuilder getUri( @Nonnull JaxbObject object, @Nonnull UriContext context ) {
    return context.getBaseUriBuilder().path( PATH_USERS ).path( object.getId() );
  }

  /**
   * Copies the fields
   *
   * @param source
   * @param target the target jaxb source the fields are set at
   * @param context    the context
   */
  @Override
  protected void copyFieldsToJaxbObject( @Nonnull com.cedarsoft.rest.sample.User source, @Nonnull User.Jaxb target, @Nonnull UriContext context ) {
    target.setEmail( source.getEmail() );
    target.setName( source.getName() );
    target.setFriends( getStub( User.Stub.class, source.getFriends(), context ) );
    target.setGroup( getStub( Group.Stub.class, source.getGroup(), context ) );

    target.setDetails( getStub( Detail.Stub.class, source.getDetails(), context ) );
  }

  /**
   * Creates a jaxb object (with id)
   *
   * @param object the object
   * @return the created jaxb object
   */
  @Override
  @Nonnull
  protected User.Jaxb createJaxbObject( @Nonnull com.cedarsoft.rest.sample.User object ) {
    return new User.Jaxb( object.getEmail() );
  }

  @Nonnull
  @Override
  protected User.Stub createJaxbStub( @Nonnull com.cedarsoft.rest.sample.User object ) {
    return new User.Stub( object.getEmail() );
  }

  @Override
  protected void copyFieldsToStub( @Nonnull com.cedarsoft.rest.sample.User source, @Nonnull User.Stub target, @Nonnull UriContext context ) {
    target.setEmail( source.getEmail() );
    target.setName( source.getName() );
  }
}
