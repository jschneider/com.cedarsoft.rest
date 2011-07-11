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

import com.cedarsoft.rest.model.JaxbObject;
import com.cedarsoft.rest.server.JaxbMapping;
import com.cedarsoft.rest.server.UriContext;
import javax.annotation.Nonnull;

import javax.ws.rs.core.UriBuilder;


/**
 *
 */
public class CameraMapping extends JaxbMapping<com.cedarsoft.rest.sample.Camera, Camera.Jaxb, Camera.Stub> {
  public CameraMapping( @Nonnull UserMapping userMapping ) {
    this.getDelegatesMapping().addMapping( User.Jaxb.class, User.Stub.class, userMapping );
  }

  @Nonnull
  @Override
  protected UriBuilder getUri( @Nonnull JaxbObject object, @Nonnull UriContext context ) {
    return context.getBaseUriBuilder().path( "devices" ).path( "cameras" ).path( object.getId() );
  }

  @Nonnull
  @Override
  protected Camera.Jaxb createJaxbObject( @Nonnull com.cedarsoft.rest.sample.Camera object ) {
    return new Camera.Jaxb( object.getId() );
  }

  @Nonnull
  @Override
  protected Camera.Stub createJaxbStub( @Nonnull com.cedarsoft.rest.sample.Camera object ) {
    return new Camera.Stub( object.getId() );
  }

  @Override
  protected void copyFieldsToJaxbObject( @Nonnull com.cedarsoft.rest.sample.Camera source, @Nonnull Camera.Jaxb target, @Nonnull UriContext context ) {
    target.setDescription( "a nice description about the camera!" );

    CameraInfo.Jaxb cameraInfo = new CameraInfo.Jaxb();
    cameraInfo.setInternalSerial( source.getCameraInfo().getInternalSerial() );
    cameraInfo.setMake( source.getCameraInfo().getMake() );
    cameraInfo.setModel( source.getCameraInfo().getModel() );
    cameraInfo.setSerial( source.getCameraInfo().getSerial() );
    target.setCameraInfo( cameraInfo );

    target.setOwner( getStub( User.Stub.class, source.getOwner(), context ) );
  }

  @Override
  protected void copyFieldsToStub( @Nonnull com.cedarsoft.rest.sample.Camera source, @Nonnull Camera.Stub target, @Nonnull UriContext context ) {
    CameraInfo.Stub cameraInfo = new CameraInfo.Stub();
    cameraInfo.setMake( source.getCameraInfo().getMake() );
    cameraInfo.setModel( source.getCameraInfo().getModel() );
    target.setCameraInfo( cameraInfo );
  }
}
