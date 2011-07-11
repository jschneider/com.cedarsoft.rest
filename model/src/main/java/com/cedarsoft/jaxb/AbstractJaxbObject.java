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

package com.cedarsoft.jaxb;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import java.net.URI;

/**
 * Abstract base class for JAXB objects that are exported using REST.
 * Every object contains an ID and a href
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlTransient
public abstract class AbstractJaxbObject implements JaxbObject {

  public static final String NS_STUB_SUFFIX = "/stub";
  @Nonnull

  public static final String NS_COLLECTION_SUFFIX = "/list";

  @XmlAttribute( required = false )
  @Nullable
  protected URI href;

  @Nullable
  @XmlAttribute( required = false )
  @XmlID

  protected final String id;

  protected AbstractJaxbObject() {
    this( null );
  }

  protected AbstractJaxbObject( @Nullable  String id ) {
    this.id = id;
  }

  @Override
  @Nonnull
  public URI getHref() {
    if ( href == null ) {
      throw new IllegalStateException( "href has not been set!" );
    }
    return href;
  }

  @Override
  public final void setHref( @Nonnull URI href ) {
    this.href = href;
  }

  boolean isHrefSet() {
    return href != null;
  }

  @Override
  @Nonnull
  public String getId() {
    if ( id == null ) {
      throw new IllegalStateException( "id has not been set" );
    }
    return id;
  }

  public boolean isIdSet() {
    return id != null;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    AbstractJaxbObject object = ( AbstractJaxbObject ) o;

    if ( id != null ? !id.equals( object.id ) : object.id != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
