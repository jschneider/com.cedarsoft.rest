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

package com.cedarsoft.rest;

import com.cedarsoft.jaxb.JaxbObject;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.theories.*;

import javax.ws.rs.core.UriBuilder;
import java.util.Arrays;

/**
 *
 */
public class FooMappingTest extends AbstractMappedJaxbTest<FooModel, Foo, FooStub> {
  public FooMappingTest() {
    super( Foo.class, FooStub.class );
  }

  @NotNull
  @Override
  protected JaxbMapping<FooModel, Foo, FooStub> createMapping() {
    return new FooMapping();
  }

  @DataPoint
  public static Entry<? extends FooModel> entry1() {
    FooModel fooModel = new FooModel( "Hello", Arrays.asList( "A", "B", "C" ), Arrays.asList( new FooModel.BarModel( 1 ), new FooModel.BarModel( 2 ) ) );
    return create( fooModel,
                   "<ns2:foo xmlns:ns2=\"test:foo\" href=\"http://test.running/here/test:daUri\" id=\"daId\">\n" +
                     "  <daValue>Hello</daValue>\n" +
                     "</ns2:foo>",
                   "<ns4:fooStub xmlns:ns4=\"test:foo/stub\" href=\"http://test.running/here/test:daUri\" id=\"daId\">\n" +
                     "</ns4:fooStub>" );
  }


  private static class FooMapping extends JaxbMapping<FooModel, Foo, FooStub> {
    @NotNull
    @Override
    protected UriBuilder getUri( @NotNull JaxbObject object, @NotNull UriContext context ) {
      return context.getBaseUriBuilder().path( "test:daUri" );
    }

    @NotNull
    @Override
    protected Foo createJaxbObject( @NotNull FooModel object ) {
      return new Foo( "daId" );
    }

    @Override
    protected void copyFieldsToJaxbObject( @NotNull FooModel source, @NotNull Foo target, @NotNull UriContext context ) {
      target.setDaValue( source.getDaValue() );
    }

    @NotNull
    @Override
    protected FooStub createJaxbStub( @NotNull FooModel object ) {
      return new FooStub("daId");
    }

    @Override
    protected void copyFieldsToStub( @NotNull FooModel source, @NotNull FooStub target, @NotNull UriContext context ) {
    }
  }
}
