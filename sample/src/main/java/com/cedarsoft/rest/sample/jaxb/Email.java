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

import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.JaxbStub;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlTransient
@XmlAccessorType( XmlAccessType.FIELD )
public abstract class Email extends AbstractJaxbObject {

  public static final String NS = "http://cedarsoft.com/rest/sample/email";

  @XmlValue
  private String address;

  protected Email() {
  }

  protected Email( String address ) {
    this( null, address );
  }

  protected Email( @Nullable  String id, String address ) {
    super( id );
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress( String address ) {
    this.address = address;
  }

  @XmlType( name = "email", namespace = Jaxb.NS )
  @XmlRootElement( name = "email", namespace = Jaxb.NS )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends Email {
    public Jaxb() {
    }

    public Jaxb( String address ) {
      super( address );
    }

    Jaxb( String id, String address ) {
      super( id, address );
    }
  }

  @XmlType( name = "emailStub", namespace = Stub.NS_STUB )
  @XmlRootElement( name = "email", namespace = Stub.NS_STUB )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends Email implements JaxbStub<Email.Jaxb> {

    public static final String NS_STUB = NS + NS_STUB_SUFFIX;

    public Stub() {
    }

    public Stub( String address ) {
      super( address );
    }

    public Stub( String id, String address ) {
      super( id, address );
    }

    @Nonnull
    @Override
    public Class<Email.Jaxb> getJaxbType() {
      return Email.Jaxb.class;
    }
  }
}
