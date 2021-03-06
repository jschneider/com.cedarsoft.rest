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

package com.cedarsoft.rest.sample;


import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class User {
  @Nonnull
  private final String email;
  @Nonnull

  private final String name;
  @Nonnull
  private final Group group;
  @Nonnull
  private final List<User> friends = new ArrayList<User>();
  @Nonnull
  private final List<Detail> details = new ArrayList<Detail>();

  public User( @Nonnull String email, @Nonnull String name ) {
    this( email, name, new Group( "NOBODY", "Nobody is in this group" ) );
  }

  public User( @Nonnull String email, @Nonnull String name, @Nonnull Group group ) {
    this.email = email;
    this.name = name;
    this.group = group;
  }

  @Nonnull
  public Group getGroup() {
    return group;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public String getEmail() {
    return email;
  }

  @Nonnull
  public List<? extends User> getFriends() {
    return Collections.unmodifiableList( friends );
  }

  public void addFriend( @Nonnull User user ) {
    this.friends.add( user );
  }

  @Nonnull
  public List<Detail> getDetails() {
    return Collections.unmodifiableList( details );
  }

  public void addDetail( @Nonnull Detail detail ) {
    this.details.add( detail );
  }

  public void setDetails( @Nonnull List<? extends Detail> details ) {
    this.details.clear();
    this.details.addAll( details );
  }
}
