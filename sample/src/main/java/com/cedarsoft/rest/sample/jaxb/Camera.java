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

import com.cedarsoft.rest.model.AbstractJaxbObject;
import com.cedarsoft.rest.model.JaxbStub;

import javax.annotation.Nonnull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
//@XmlType( name = "abstractCamera" )
@XmlTransient
public abstract class Camera extends AbstractJaxbObject {

  public static final String NS = "http://cedarsoft.com/rest/sample/camera";

  protected Camera() {
  }

  protected Camera( @Nonnull  String id ) {
    super( id );
  }

  @XmlType( name = "camera", namespace = Jaxb.NS )
  @XmlRootElement( name = "camera", namespace = Jaxb.NS )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends Camera {
    private CameraInfo.Jaxb cameraInfo;
    private String description;
    private User.Stub owner;

    public Jaxb() {
    }

    public Jaxb( @Nonnull  String id ) {
      super( id );
    }

    public CameraInfo.Jaxb getCameraInfo() {
      return cameraInfo;
    }

    public void setCameraInfo( CameraInfo.Jaxb cameraInfo ) {
      this.cameraInfo = cameraInfo;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription( String description ) {
      this.description = description;
    }

    public User.Stub getOwner() {
      return owner;
    }

    public void setOwner( User.Stub owner ) {
      this.owner = owner;
    }
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  @XmlType( name = "cameraStub", namespace = Stub.NS_STUB )
  @XmlRootElement( name = "camera", namespace = Stub.NS_STUB )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends Camera implements JaxbStub<Jaxb> {

    public static final String NS_STUB = NS + NS_STUB_SUFFIX;

    private CameraInfo.Stub cameraInfo;

    public Stub() {
    }

    public Stub( @Nonnull  String id ) {
      super( id );
    }

    public CameraInfo.Stub getCameraInfo() {
      return cameraInfo;
    }

    public void setCameraInfo( CameraInfo.Stub cameraInfo ) {
      this.cameraInfo = cameraInfo;
    }

    @Nonnull
    @Override
    public Class<Jaxb> getJaxbType() {
      return Jaxb.class;
    }
  }
}
