/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JerseyModule extends ServletModule {
  @NotNull
  @NonNls
  private final Map<String, String> params = new HashMap<String, String>();
  @NonNls
  public static final String SEPARATOR = ";";

  public JerseyModule() {
    enableFeatureXmlRootProcessing();
  }

  public JerseyModule( @NotNull @NonNls List<? extends Package> resourcePackages ) {
    this( resourcePackages, Collections.<String, String>emptyMap() );
  }

  public JerseyModule( @NotNull @NonNls List<? extends Package> resourcePackages, @NotNull @NonNls Map<? extends String, ? extends String> params ) {
    this();
    addResourcePackages( resourcePackages );

    enableFeatureXmlRootProcessing();
    this.params.putAll( params );
  }

  public JerseyModule( @NotNull @NonNls Class<?> resourceType, @NotNull @NonNls Map<? extends String, ? extends String> params ) {
    this();
    addResource( resourceType );
    addParam( ServletContainer.RESOURCE_CONFIG_CLASS, ClassNamesResourceConfig.class.getName() );

    this.params.putAll( params );
  }

  public final void addResourcePackages( @NotNull List<? extends Package> resourcePackages ) {
    List<String> packageNames = Lists.transform( resourcePackages, new Function<Package, String>() {
      @Override
      public String apply( Package from ) {
        return from.getName();
      }
    } );

    addParamElement( PackagesResourceConfig.PROPERTY_PACKAGES, Joiner.on( " " ).join( packageNames ) );
  }

  public final void addResource( @NotNull Class<?> resourceType ) {
    addParamElement( ClassNamesResourceConfig.PROPERTY_CLASSNAMES, resourceType.getName() );
  }

  public final void addResources( @NotNull Class<?>... resourceTypes ) {
    addResources( Arrays.asList( resourceTypes ) );
  }

  public final void addResources( @NotNull List<? extends Class<?>> resourceTypes ) {
    List<String> classNames = Lists.transform( resourceTypes, new Function<Class<?>, String>() {
      @Override
      public String apply( Class<?> from ) {
        return from.getName();
      }
    } );

    addParamElement( ClassNamesResourceConfig.PROPERTY_CLASSNAMES, Joiner.on( SEPARATOR ).join( classNames ) );
  }

  public void addResourceFilterFactory( @NotNull Class<? extends ResourceFilterFactory> resourceFilterFactoryType ) {
    addParamElement( DefaultResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, resourceFilterFactoryType.getName() );
  }

  public final void enableFeatureXmlRootProcessing() {
    addParam( FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, "true" );
  }

  public void addParamElement( @NotNull @NonNls String key, @NotNull @NonNls String additionalValue ) {
    String old = params.get( key );
    if ( old == null ) {
      addParam( key, additionalValue );
    } else {
      params.put( key, old + SEPARATOR + additionalValue );
    }
  }

  /**
   * Adds a parameter
   *
   * @param key   the key
   * @param value the value
   */
  public final void addParam( @NotNull @NonNls String key, @NotNull @NonNls String value ) {
    params.put( key, value );
  }

  @Override
  protected void configureServlets() {
    serve( "/*" ).with( GuiceContainer.class, params );
  }
}
