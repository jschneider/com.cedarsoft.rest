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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

/**
 *
 */
@XmlRootElement( namespace = Link.NAMESPACE_XLINK )
@XmlType( namespace = Link.NAMESPACE_XLINK )
@XmlAccessorType( XmlAccessType.FIELD )
public class Link extends AbstractJaxbObject {
  @NonNls
  @NotNull
  public static final String NAMESPACE_XLINK = "http://www.w3.org/1999/xlink";
  @NotNull
  @NonNls
  public static final String SELF = "self";
  @NotNull
  @NonNls
  public static final String ENCLOSURE = "enclosure";

  @Nullable
  @NonNls
  @XmlAttribute
  private String type;

  public Link() {
  }

  public Link( @Nullable @NonNls String id ) {
    super( id );
  }

  public Link( @NotNull URI href, @NotNull @NonNls String type ) {
    this( null, href, type );
  }

  public Link( @Nullable @NonNls String id, @NotNull URI href, @NotNull @NonNls String type ) {
    super( id );
    this.type = type;
    setHref( href );
  }

  @NotNull
  @NonNls
  public String getType() {
    if ( type == null ) {
      throw new IllegalStateException( "No type has been set" );
    }
    return type;
  }

  /**
   *
   */
  @XmlRootElement( name = "link" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends AbstractJaxbObject implements JaxbStub<Link> {
    public Stub() {
    }

    public Stub( @Nullable @NonNls String id, @NotNull URI href ) {
      super( id );
      setHref( href );
    }

    public Stub( @NotNull URI href ) {
      setHref( href );
    }

    @NotNull
    @Override
    public Class<Link> getJaxbType() {
      return Link.class;
    }
  }
}
