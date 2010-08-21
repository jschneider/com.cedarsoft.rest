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

package com.cedarsoft.rest;

import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.jaxb.JaxbStub;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DelegateJaxbMappings {
  @NotNull
  private final Map<Class<? extends JaxbObject>, JaxbMapping<?, ?, ?>> delegates = new HashMap<Class<? extends JaxbObject>, JaxbMapping<?, ?, ?>>();
  @NotNull
  private final Map<Class<? extends JaxbObject>, JaxbMapping<?, ?, ?>> delegatesByStub = new HashMap<Class<? extends JaxbObject>, JaxbMapping<?, ?, ?>>();

  public <T, J extends JaxbObject, S extends JaxbStub> void addMapping( @NotNull Class<J> jaxbObjectType, @NotNull Class<S> jaxbStubType, @NotNull JaxbMapping<T, J, S> mapping ) {
    delegates.put( jaxbObjectType, mapping );
    delegatesByStub.put( jaxbStubType, mapping );
  }

  @NotNull
  public <T, J extends JaxbObject, S extends JaxbStub> JaxbMapping<T, J, S> getMapping( @NotNull Class<J> jaxbObjectType ) {
    JaxbMapping<?, ?, ?> resolved = delegates.get( jaxbObjectType );
    if ( resolved == null ) {
      throw new IllegalArgumentException( "No mapping found for " + jaxbObjectType.getName() );
    }
    return ( JaxbMapping<T, J, S> ) resolved;
  }

  @NotNull
  public <T, J extends JaxbObject, S extends JaxbStub> JaxbMapping<T, J, S> getMappingForStub( @NotNull Class<S> jaxbStubType ) {
    JaxbMapping<?, ?, ?> resolved = delegatesByStub.get( jaxbStubType );
    if ( resolved == null ) {
      throw new IllegalArgumentException( "No mapping found for " + jaxbStubType.getName() );
    }
    return ( JaxbMapping<T, J, S> ) resolved;
  }

}
