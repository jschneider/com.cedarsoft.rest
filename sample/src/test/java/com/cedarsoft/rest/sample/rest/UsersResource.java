package com.cedarsoft.rest.sample.rest;

import com.cedarsoft.rest.sample.jaxb.User;
import com.cedarsoft.rest.sample.jaxb.UserMapping;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Path( UserMapping.PATH_USERS )
@Produces( {MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON} )
public class UsersResource {
  @NotNull
  @Context
  private UriInfo uriInfo;
  @Context
  private Request request;
  @NotNull
  private final List<com.cedarsoft.rest.sample.User> users;
  @NotNull
  private final UserMapping userMapping;

  @Inject
  public UsersResource( @NotNull UserMapping userMapping, @NotNull List<? extends com.cedarsoft.rest.sample.User> users ) throws IOException {
    FileUtils.touch( new File( "/tmp/UsersResources" ) );
    this.userMapping = userMapping;
    this.users = new ArrayList<com.cedarsoft.rest.sample.User>( users );
  }

  @GET
  public List<User.Stub> getUsers( @Context HttpHeaders headers, @QueryParam( "minId" ) int minId, @QueryParam( "max-id" ) int maxId ) throws URISyntaxException {
    return userMapping.getJaxbStubs( users, uriInfo.getBaseUriBuilder() );
  }

  @GET
  @Path( "test" )
  public User.Jaxb getUser() throws URISyntaxException {
    com.cedarsoft.rest.sample.User user = new com.cedarsoft.rest.sample.User( "test@test.com", "Test User" );
    user.addFriend( new com.cedarsoft.rest.sample.User( "friend@asdf.de", "A Friend" ) );
    return userMapping.getJaxbObject( user, uriInfo.getBaseUriBuilder() );
  }
}
