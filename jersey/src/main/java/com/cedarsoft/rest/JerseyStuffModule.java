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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.MessageBodyWorkers;
import com.sun.jersey.spi.container.ExceptionMapperContext;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.WebComponent;
import org.fest.reflect.core.Reflection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 */
public class JerseyStuffModule extends AbstractModule {
  @Override
  protected void configure() {
  }

  @Nullable
  private transient WebApplication webApplicationReference;

  @Provides
  public WebApplication webApp( @Nonnull GuiceContainer guiceContainer ) {
    WebApplication copy = webApplicationReference;
    if ( copy == null ) {
      WebComponent component = Reflection.field( "webComponent" ).ofType( WebComponent.class ).in( guiceContainer ).get();
      copy = Reflection.field( "application" ).ofType( WebApplication.class ).in( component ).get();
      webApplicationReference = copy;
    }
    return copy;
  }

  @RequestScoped
  @Provides
  public HttpContext httpContext( @Nonnull WebApplication webApplication ) {
    return webApplication.getThreadLocalHttpContext();
  }

  @Provides
  public ExceptionMapperContext exceptionMapperContext( @Nonnull WebApplication webApplication ) {
    return webApplication.getExceptionMapperContext();
  }

  @Provides
  public FeaturesAndProperties featuresAndProperties( @Nonnull WebApplication webApplication ) {
    return webApplication.getFeaturesAndProperties();
  }

  @Provides
  public ResourceConfig resourceConfig( @Nonnull WebApplication webApplication ) {
    return ( ResourceConfig ) webApplication.getFeaturesAndProperties();
  }

  @Provides
  public MessageBodyWorkers messageBodyFactory( @Nonnull WebApplication webApplication ) {
    return webApplication.getMessageBodyWorkers();
  }

  @RequestScoped
  @Provides
  public UriInfo uriInfo( @Nonnull HttpContext httpContext ) {
    return httpContext.getUriInfo();
  }

  @RequestScoped
  @Provides
  public HttpRequestContext requestContext( @Nonnull HttpContext httpContext ) {
    return httpContext.getRequest();
  }

  @RequestScoped
  @Provides
  public HttpHeaders httpHeaders( @Nonnull HttpContext httpContext ) {
    return httpContext.getRequest();
  }

  @RequestScoped
  @Provides
  public Request request( @Nonnull HttpContext httpContext ) {
    return httpContext.getRequest();
  }

  @RequestScoped
  @Provides
  public SecurityContext securityContext( @Nonnull HttpContext httpContext ) {
    return httpContext.getRequest();
  }

  @RequestScoped
  @Provides
  public UriBuilder uriBuilder( @Nonnull UriInfo uriInfo ) {
    return uriInfo.getRequestUriBuilder();
  }
}
