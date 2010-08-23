package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.rest.sample.User;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Path( UserJaxbMapping.PATH_USERS )
@Produces( {MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON} )
public class UsersResource {
  @NotNull
  @Context
  private UriInfo uriInfo;
  @Context
  private Request request;
  @NotNull
  private final List<User> users;
  @NotNull
  private final UserJaxbMapping userMapping;

  @Inject
  public UsersResource( @NotNull UserJaxbMapping userMapping, @NotNull List<? extends User> users ) throws IOException {
    FileUtils.touch( new File( "/tmp/UsersResources" ) );
    this.userMapping = userMapping;
    this.users = new ArrayList<User>( users );
  }

  @GET
  public List<UserJaxb.Stub> getUsers( @Context HttpHeaders headers, @QueryParam( "minId" ) int minId, @QueryParam( "max-id" ) int maxId ) throws URISyntaxException {
    return userMapping.getJaxbStubs( users, uriInfo.getBaseUriBuilder() );
  }

  @GET
  @Path( "test" )
  public UserJaxb.Complete getUser() throws URISyntaxException {
    User user = new User( "test@test.com", "Test User" );
    user.addFriend( new User( "friend@asdf.de", "A Friend" ) );
    return userMapping.getJaxbObject( user, uriInfo.getBaseUriBuilder() );
  }
}
