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
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;


/**
 *
 */
public class CameraMapping extends JaxbMapping<com.cedarsoft.rest.sample.Camera, Camera.Jaxb, Camera.Stub> {
  public CameraMapping( @NotNull UserMapping userMapping ) {
    this.getDelegatesMapping().addMapping( User.Jaxb.class, User.Stub.class, userMapping );
  }

  @Override
  protected void setUris( @NotNull JaxbObject object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
    object.setHref( uriBuilder.path( "devices" ).path( "cameras" ).path( "{id}" ).build( object.getId() ) );
  }

  @NotNull
  @Override
  protected Camera.Jaxb createJaxbObject( @NotNull com.cedarsoft.rest.sample.Camera object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    Camera.Jaxb jaxbObject = new Camera.Jaxb();

    jaxbObject.setId( object.getId() );
    jaxbObject.setDescription( "a nice description about the camera!" );

    CameraInfo.Jaxb cameraInfo = new CameraInfo.Jaxb();
    cameraInfo.setInternalSerial( object.getCameraInfo().getInternalSerial() );
    cameraInfo.setMake( object.getCameraInfo().getMake() );
    cameraInfo.setModel( object.getCameraInfo().getModel() );
    cameraInfo.setSerial( object.getCameraInfo().getSerial() );
    jaxbObject.setCameraInfo( cameraInfo );

    jaxbObject.setOwner( getStub( User.Stub.class, object.getOwner(), context ) );

    return jaxbObject;
  }

  @Override
  protected Camera.Stub createJaxbObjectStub( @NotNull com.cedarsoft.rest.sample.Camera object, @NotNull UriContext context ) throws URISyntaxException {
    Camera.Stub jaxbObject = new Camera.Stub();
    jaxbObject.setId( object.getId() );

    CameraInfo.Stub cameraInfo = new CameraInfo.Stub();
    cameraInfo.setMake( object.getCameraInfo().getMake() );
    cameraInfo.setModel( object.getCameraInfo().getModel() );
    jaxbObject.setCameraInfo( cameraInfo );

    return jaxbObject;
  }
}
