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

package com.cedarsoft.rest.test;

import javax.annotation.Nonnull;
import org.junit.rules.*;
import org.junit.runners.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JaxbRule implements MethodRule {
  private final List<Class<?>> typesToBeBound = new ArrayList<Class<?>>();

  public JaxbRule( @Nonnull Class<?>... typesToBeBound ) {
    for ( Class<?> typeToBeBound : typesToBeBound ) {
      if ( typeToBeBound == null ) {
        continue;

      }
      this.typesToBeBound.add( typeToBeBound );
    }
  }

  public JaxbRule( @Nonnull Collection<? extends Class<?>> typesToBeBound ) {
    this.typesToBeBound.addAll( typesToBeBound );
  }

  @Override
  public Statement apply( final Statement base, FrameworkMethod method, Object target ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        prepareContext();
        try {
          base.evaluate();
        } finally {
          clearContext();
        }
      }
    };
  }

  @Nonnull
  public Marshaller createMarshaller() throws JAXBException {
    return getContext().createMarshaller();
  }

  @Nonnull
  public Unmarshaller createUnmarshaller() throws JAXBException {
    return getContext().createUnmarshaller();
  }

  @Nonnull
  public JAXBContext getContext() throws JAXBException {
    if ( context == null ) {
      context = createContext();
    }
    return context;
  }

  public void addTypeToBeBound( @Nonnull Class<?> type ) {
    this.typesToBeBound.add( type );
  }

  public List<? extends Class<?>> getTypesToBeBound() {
    return Collections.unmodifiableList( typesToBeBound );
  }

  @Nonnull
  protected JAXBContext createContext() throws JAXBException {
    return JAXBContext.newInstance( typesToBeBound.toArray( new Class[new ArrayList<Class<?>>( typesToBeBound ).size()] ) );
  }

  private void prepareContext() throws JAXBException {
  }

  private void clearContext() {
    context = null;
  }

  protected JAXBContext context;
}
