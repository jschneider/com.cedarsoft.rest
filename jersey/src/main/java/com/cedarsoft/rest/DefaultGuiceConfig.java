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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Default Guice Configuration that can be used for Jersey/Guice stuff.
 * <p/>
 * The corresponding section of your web.xml could look like that:
 * <pre>&lt;listener&gt;<br/>  &lt;listener-class&gt;com.cedarsoft.myapp.MyGuiceConfig&lt;/listener-class&gt;<br/>&lt;/listener&gt;<br/>&lt;filter&gt;<br/>  &lt;filter-name&gt;guiceFilter&lt;/filter-name&gt;<br/>  &lt;filter-class&gt;com.google.inject.servlet.GuiceFilter&lt;/filter-class&gt;<br/>&lt;/filter&gt;<br/>&lt;filter-mapping&gt;<br/>  &lt;filter-name&gt;guiceFilter&lt;/filter-name&gt;<br/>  &lt;url-pattern&gt;/*&lt;/url-pattern&gt;<br/>&lt;/filter-mapping&gt;<br/>
 * </pre>
 */
public class DefaultGuiceConfig extends GuiceServletContextListener {
  @NotNull
  protected final List<Module> modules = new ArrayList<Module>();
  @NotNull
  private final JerseyModule jerseyModule;

  public DefaultGuiceConfig( @NotNull Class<?> resourceType ) {
    this( Collections.<String, String>emptyMap(), resourceType );
  }

  public DefaultGuiceConfig( @NotNull @NonNls Package... resourcePackages ) {
    this( Collections.<String, String>emptyMap(), resourcePackages );
  }

  public DefaultGuiceConfig( @NotNull @NonNls Map<String, String> params, @NotNull @NonNls Package... resourcePackages ) {
    this( new JerseyModule( Arrays.asList( resourcePackages ), params ) );
  }

  public DefaultGuiceConfig( @NotNull @NonNls Map<String, String> params, @NotNull @NonNls Class<?> resourceType ) {
    this( new JerseyModule( resourceType, params ) );
  }

  public DefaultGuiceConfig( @NotNull JerseyModule jerseyModule ) {
    this.jerseyModule = jerseyModule;
    modules.add( this.jerseyModule );
    modules.add( new JerseyStuffModule() );
  }

  public void addModule( @NotNull Module module ) {
    this.modules.add( module );
  }

  public void addModules( @NotNull Collection<? extends Module> additionalModules ) {
    this.modules.addAll( additionalModules );
  }

  @NotNull
  public JerseyModule getJersey() {
    return jerseyModule;
  }

  @Override
  protected Injector getInjector() {
    return Guice.createInjector( modules );
  }
}
