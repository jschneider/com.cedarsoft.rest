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

package com.cedarsoft.rest.sample.rest;

import com.cedarsoft.rest.DefaultGuiceConfig;
import com.cedarsoft.rest.sample.Detail;
import com.cedarsoft.rest.sample.User;
import com.cedarsoft.rest.sample.jaxb.UserMapping;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Map;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MyGuiceConfig extends DefaultGuiceConfig {
  public MyGuiceConfig() {
    super( UsersResource.class );

//    getJersey().addResource( JacksonJaxbJsonProvider.class );
    getJersey().addResource( MyJacksonProvider.class );

    addModule( new ExampleModule() );
  }

  public static class ExampleModule extends AbstractModule {
    public ExampleModule() {
      System.out.println( "---------------" );
      System.out.println( "---------------" );
      System.out.println( "MyGuiceConfig$ExampleModule.ExampleModule" );
      System.out.println( "---------------" );
      System.out.println( "---------------" );
    }

    @Override
    protected void configure() {
      bind( UserMapping.class ).in( Singleton.class );
    }

    @Provides
    List<? extends User> provideUsers() {
      User js = new User( "info@cedarsoft.de", "Johannes Schneider" );
      js.addDetail( new Detail( "1", "A detail for Johannes" ) );
      js.addDetail( new Detail( "2", "A second detail for Johannes" ) );

      User max = new User( "markus@mustermann.de", "Markus Mustermann" );
      max.addDetail( new Detail( "1", "A max detail" ) );
      User eva = new User( "eva@mustermann.de", "Eva Mustermann" );

      js.addFriend( max );
      js.addFriend( eva );
      eva.addFriend( max );
      max.addFriend( eva );
      max.addFriend( js );

      return ImmutableList.of(
        js,
        max,
        eva
      );
    }

    //    @Nullable
    //    private transient WebApplication webApplicationReference;
    //
    //    @Provides
    //    public WebApplication webApp( @Nonnull GuiceContainer guiceContainer ) {
    //      WebApplication copy = webApplicationReference;
    //      if ( copy == null ) {
    //        WebComponent component = Reflection.field( "webComponent" ).ofType( WebComponent.class ).in( guiceContainer ).get();
    //        copy = Reflection.field( "application" ).ofType( WebApplication.class ).in( component ).get();
    //        webApplicationReference = copy;
    //      }
    //      return copy;
    //    }
    //
    //    @RequestScoped
    //    @Provides
    //    public HttpContext httpContext( @Nonnull WebApplication webApplication ) {
    //      return webApplication.getThreadLocalHttpContext();
    //    }
    //
    //    @Provides
    //    public ExceptionMapperContext exceptionMapperContext( @Nonnull WebApplication webApplication ) {
    //      return webApplication.getExceptionMapperContext();
    //    }
    //
    //    @Provides
    //    public FeaturesAndProperties featuresAndProperties( @Nonnull WebApplication webApplication ) {
    //      return webApplication.getFeaturesAndProperties();
    //    }
    //
    //    @Provides
    //    public ResourceConfig resourceConfig( @Nonnull WebApplication webApplication ) {
    //      return ( ResourceConfig ) webApplication.getFeaturesAndProperties();
    //    }
    //
    //    @Provides
    //    public MessageBodyWorkers messageBodyFactory( @Nonnull WebApplication webApplication ) {
    //      return webApplication.getMessageBodyWorkers();
    //    }
    //
    //    @RequestScoped
    //    @Provides
    //    public UriInfo uriInfo( @Nonnull HttpContext httpContext ) {
    //      return httpContext.getUriInfo();
    //    }
    //
    //    @RequestScoped
    //    @Provides
    //    public HttpRequestContext requestContext( @Nonnull HttpContext httpContext ) {
    //      return httpContext.getRequest();
    //    }
    //
    //    @RequestScoped
    //    @Provides
    //    public HttpHeaders httpHeaders( @Nonnull HttpContext httpContext ) {
    //      return httpContext.getRequest();
    //    }
    //
    //    @RequestScoped
    //    @Provides
    //    public Request request( @Nonnull HttpContext httpContext ) {
    //      return httpContext.getRequest();
    //    }
    //
    //    @RequestScoped
    //    @Provides
    //    public SecurityContext securityContext( @Nonnull HttpContext httpContext ) {
    //      return httpContext.getRequest();
    //    }
    //
    //    @RequestScoped
    //    @Provides
    //    public UriBuilder uriBuilder( @Nonnull UriInfo uriInfo ) {
    //      return uriInfo.getRequestUriBuilder();
    //    }
  }

  public static class JerseyGuiceServletModule extends ServletModule {
    private final Map<String, String> params;

    public JerseyGuiceServletModule( @Nonnull  Map<String, String> params ) {
      this.params = params;
    }

    @Override
    protected void configureServlets() {
      serve( "/*" ).with( GuiceContainer.class, params );
    }
  }
}

