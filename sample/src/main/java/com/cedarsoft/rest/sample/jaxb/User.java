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

import com.cedarsoft.jaxb.AbstractJaxbCollection;
import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.JaxbStub;

import javax.annotation.Nonnull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlTransient
@XmlAccessorType( XmlAccessType.FIELD )
public abstract class User extends AbstractJaxbObject {

  public static final String NS = "http://cedarsoft.com/rest/sample/user";

  private String email;
  private String name;

  protected User() {
  }

  protected User( @Nonnull  String id ) {
    super( id );
  }

  public String getEmail() {
    return email;
  }

  public void setEmail( String email ) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  @XmlType( name = "user", namespace = Jaxb.NS )
  @XmlRootElement( name = "user", namespace = Jaxb.NS )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends User {
    private Group.Stub group;
    @XmlElement( name = "friend" )
    private List<com.cedarsoft.rest.sample.jaxb.User.Stub> friends;
    @XmlElement( name = "detail" )
    private List<com.cedarsoft.rest.sample.jaxb.Detail.Stub> details;

    public Jaxb() {
    }

    public Jaxb( @Nonnull  String id ) {
      super( id );
    }

    public Group.Stub getGroup() {
      return group;
    }

    public void setGroup( Group.Stub group ) {
      this.group = group;
    }

    public List<com.cedarsoft.rest.sample.jaxb.User.Stub> getFriends() {
      return friends;
    }

    public void setFriends( List<com.cedarsoft.rest.sample.jaxb.User.Stub> friends ) {
      this.friends = friends;
    }

    public List<Detail.Stub> getDetails() {
      return details;
    }

    public void setDetails( List<Detail.Stub> details ) {
      this.details = details;
    }
  }

  @XmlType( name = "userStub", namespace = Stub.NS_STUB )
  @XmlRootElement( name = "user", namespace = Stub.NS_STUB )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends User implements JaxbStub<User.Jaxb> {

    public static final String NS_STUB = NS + NS_STUB_SUFFIX;

    public Stub() {
    }

    public Stub( @Nonnull  String id ) {
      super( id );
    }

    @Override
    public Class<User.Jaxb> getJaxbType() {
      return User.Jaxb.class;
    }
  }

  @XmlType( name = "users", namespace = Collection.NS_COLLECTION )
  @XmlRootElement( name = "users", namespace = Collection.NS_COLLECTION )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Collection extends AbstractJaxbCollection {

    public static final String NS_COLLECTION = Stub.NS + NS_COLLECTION_SUFFIX;

    @XmlElementRef
    private List<Stub> users;

    public Collection() {
    }

    public Collection( @Nonnull List<Stub> users ) {
      this( users, 0, 0 );
    }

    public Collection( List<Stub> users, int startIndex, int maxLength ) {
      super( startIndex, maxLength );
      this.users = users;
    }

    public List<Stub> getUsers() {
      return users;
    }

    public void setUsers( List<Stub> stubs ) {
      this.users = stubs;
    }
  }
}
