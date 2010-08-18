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

import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbTestUtils;
import com.cedarsoft.rest.SimpleJaxbTest;
import org.junit.experimental.theories.*;

public class CameraJaxbTest
  extends SimpleJaxbTest<CameraJaxb> {


  @Override
  protected Class<CameraJaxb> getJaxbType() {
    return CameraJaxb.class;
  }

  @DataPoint
  public static Entry<? extends CameraJaxb> entry1() {
    CameraJaxb object = new CameraJaxb();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setId( "id" );
    CameraInfoJaxb info = new CameraInfoJaxb();
    object.setCameraInfo( info );
    info.setInternalSerial( "INTERNAL_35138574" );
    info.setSerial( 35138574 );
    info.setMake( "Canon" );
    info.setModel( "EOS 7D" );
    object.setDescription( "description" );
    UserJaxb owner = new UserJaxb();
    owner.setEmail( "mail@mail.com" );
    owner.setName( "daName" );
    object.setOwner( owner );

    return create( object, CameraJaxbTest.class.getResource( "CameraJaxbTest.xml" ) );
  }

  @DataPoint
  public static Entry<? extends CameraJaxb> entry2() {
    CameraJaxb object = new CameraJaxb();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setId( "id" );
    CameraInfoJaxb cameraInfoJaxb = new CameraInfoJaxb();

    cameraInfoJaxb.setId( "camInfoId" );
    cameraInfoJaxb.setHref( JaxbTestUtils.createTestUriBuilder().path( "camInfo" ).build() );
    cameraInfoJaxb.setSerial( 35138574 );

    object.setCameraInfo( cameraInfoJaxb );
    object.setDescription( "description" );

    return create( object, CameraJaxbTest.class.getResource( "CameraJaxbTest.2.xml" ) );
  }
}
