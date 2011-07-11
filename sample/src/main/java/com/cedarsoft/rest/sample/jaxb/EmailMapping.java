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
import com.cedarsoft.rest.sample.Email;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.ws.rs.core.UriBuilder;

public class EmailMapping extends JaxbMapping<Email, com.cedarsoft.rest.sample.jaxb.Email.Jaxb, com.cedarsoft.rest.sample.jaxb.Email.Stub> {
  @Nonnull

  public static final String PATH = "emails";

  @Nullable
  @Override
  protected UriBuilder getUri( @Nonnull JaxbObject object, @Nonnull UriContext context ) {
    return null;
  }

  @Nonnull
  @Override
  protected com.cedarsoft.rest.sample.jaxb.Email.Jaxb createJaxbObject( @Nonnull Email object ) {
    return new com.cedarsoft.rest.sample.jaxb.Email.Jaxb( object.getAddress() );
  }

  @Nonnull
  @Override
  protected com.cedarsoft.rest.sample.jaxb.Email.Stub createJaxbStub( @Nonnull Email object ) {
    return new com.cedarsoft.rest.sample.jaxb.Email.Stub( object.getAddress() );
  }

  @Override
  protected void copyFieldsToJaxbObject( @Nonnull Email source, @Nonnull com.cedarsoft.rest.sample.jaxb.Email.Jaxb target, @Nonnull UriContext context ) {
    target.setAddress( source.getAddress() );
  }

  @Override
  protected void copyFieldsToStub( @Nonnull Email source, @Nonnull com.cedarsoft.rest.sample.jaxb.Email.Stub target, @Nonnull UriContext context ) {
    target.setAddress( source.getAddress() );
  }

}
