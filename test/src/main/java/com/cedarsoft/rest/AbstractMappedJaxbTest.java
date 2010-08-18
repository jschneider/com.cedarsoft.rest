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
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import org.junit.experimental.theories.*;
import org.junit.runner.*;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

@RunWith( Theories.class )
public abstract class AbstractMappedJaxbTest<T, J extends JaxbObject> extends AbstractJaxbTest<J> {
  protected JaxbMapping<T, J> mapping;

  @Override
  @Before
  public void setup() throws JAXBException {
    super.setup();
    mapping = createMapping();
  }

  @NotNull
  protected abstract JaxbMapping<T, J> createMapping();

  @Theory
  public void testRoundTripWithDataPoints( @NotNull Entry<? extends T> entry ) throws Exception {
    Marshaller marshaller = createMarshaller();
    marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );

    T object = entry.getObject();
    J jaxbObject = createJaxbObject( object );

    assertNotNull( jaxbObject.getHref() );
    assertNotNull( jaxbObject.getId() );

    StringWriter out = new StringWriter();
    marshaller.marshal( jaxbObject, out );

    AssertUtils.assertXMLEquals( new String( entry.getExpected() ), out.toString() );

    J deserialized = getJaxbType().cast( createUnmarshaller().unmarshal( new StringReader( out.toString() ) ) );
    assertNotNull( deserialized );

    verifyDeserialized( deserialized, jaxbObject );
  }

  @NotNull
  protected J createJaxbObject( @NotNull T object ) throws URISyntaxException {
    return createMapping().getJaxbObject( object, JaxbTestUtils.createTestUriBuilder() );
  }

  protected void verifyDeserialized( @NotNull J deserialized, @NotNull J originalJaxbObject ) throws IllegalAccessException {
    assertEquals( originalJaxbObject, deserialized );
  }
}
