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

import com.cedarsoft.AssertUtils;
import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.jaxb.JaxbStub;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.theories.*;
import org.junit.runner.*;

import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * @param <J> the object to serialize
 * @param <S> the stub type
 */
@RunWith( Theories.class )
public abstract class SimpleJaxbTest<J extends JaxbObject, S extends JaxbStub<J>> extends AbstractJaxbTest<J, S> {
  protected SimpleJaxbTest( @NotNull Class<J> jaxbType, @NotNull Class<S> jaxbStubType ) {
    super( jaxbType, jaxbStubType );
  }

  @Theory
  public void testRoundTripWithDataPoints( @NotNull Entry<? extends J> entry ) throws Exception {
    if ( !isJaxbObjectType( entry ) ) {
      return;
    }

    Marshaller marshaller = createMarshaller();
    marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );

    J jaxbObject = entry.getObject();
    assertEquals( getJaxbType(), jaxbObject.getClass() );

    assertNotNull( jaxbObject.getHref() );
    assertNotNull( jaxbObject.getId() );

    StringWriter out = new StringWriter();
    marshaller.marshal( jaxbObject, out );

    AssertUtils.assertXMLEquals( new String( entry.getExpected() ), out.toString() );

    Object unserializedRaw = createUnmarshaller().unmarshal( new StringReader( out.toString() ) );
    assertNotNull( unserializedRaw );
    assertEquals( getJaxbType(), unserializedRaw.getClass() );

    J deserialized = getJaxbType().cast( unserializedRaw );
    assertNotNull( deserialized );

    verifyDeserialized( deserialized, jaxbObject );
  }

  @Theory
  public void testRoundStub( @NotNull Entry<? extends S> entry ) throws Exception {
    if ( !isJaxbStubType( entry ) ) {
      return;
    }

    Marshaller marshaller = createMarshaller();
    marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );

    S jaxbStub = entry.getObject();
    assertEquals( getJaxbStubType(), jaxbStub.getClass() );

    assertNotNull( jaxbStub.getHref() );
    assertNotNull( jaxbStub.getId() );

    StringWriter out = new StringWriter();
    marshaller.marshal( jaxbStub, out );

    AssertUtils.assertXMLEquals( new String( entry.getStubExpected() ), out.toString() );

    S deserialized = getJaxbStubType().cast( createUnmarshaller().unmarshal( new StringReader( out.toString() ) ) );
    assertNotNull( deserialized );

    verifyDeserializedStub( deserialized, jaxbStub );
  }
}
