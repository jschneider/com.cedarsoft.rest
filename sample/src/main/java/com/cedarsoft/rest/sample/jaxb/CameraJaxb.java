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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.cedarsoft.jaxb.AbstractJaxbObject;

@XmlRootElement( name = "camera", namespace = "http://cedarsoft.com/rest/sample/camera" )
@XmlAccessorType( XmlAccessType.FIELD )
public class CameraJaxb
    extends AbstractJaxbObject
{

    private CameraInfoJaxb cameraInfo;
    private String description;
    private UserJaxbStub owner;

  public CameraInfoJaxb getCameraInfo() {
    return cameraInfo;
  }

  public void setCameraInfo( CameraInfoJaxb cameraInfo ) {
    this.cameraInfo = cameraInfo;
  }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserJaxbStub getOwner() {
        return owner;
    }

    public void setOwner(UserJaxbStub owner) {
        this.owner = owner;
    }

}
