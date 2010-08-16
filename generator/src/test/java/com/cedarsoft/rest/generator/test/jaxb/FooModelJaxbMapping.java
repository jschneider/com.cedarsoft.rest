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

package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.JaxbMappingContext;
import com.cedarsoft.rest.generator.test.FooModel;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 */
public class FooModelJaxbMapping extends JaxbMapping<FooModel, FooModelJaxb> {
  public FooModelJaxbMapping( @NotNull BarModelJaxbMapping barModelJaxbMapping ) {
    getDelegatesMapping().addMapping( BarModelJaxb.class, barModelJaxbMapping );
  }

  @Override
  protected void setUris( @NotNull FooModelJaxb object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
    object.setHref( uriBuilder.path( "daHrefForFoo" ).build() );
  }

  @NotNull
  @Override
  protected FooModelJaxb createJaxbObject( @NotNull FooModel object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    FooModelJaxb jaxbModel = new FooModelJaxb();
    jaxbModel.setId( object.getId() );

    jaxbModel.setSingleBar( get( BarModelJaxb.class, object.getSingleBar(), context ) );
    jaxbModel.setTheBars( getList( BarModelJaxb.class, object.getTheBars(), context ) );
    return jaxbModel;
  }
}
