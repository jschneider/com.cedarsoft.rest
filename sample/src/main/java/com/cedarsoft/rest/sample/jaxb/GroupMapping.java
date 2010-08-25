package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.JaxbMappingContext;
import com.cedarsoft.rest.sample.Group;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;

public class GroupMapping
  extends JaxbMapping<Group, com.cedarsoft.rest.sample.jaxb.Group.Jaxb, com.cedarsoft.rest.sample.jaxb.Group.Stub> {

  public static final String PATH = "groups";
  public static final String ID = "{id}";

  @Override
  protected void setUris( @NotNull JaxbObject object, @NotNull UriBuilder uriBuilder ) {
    object.setHref( uriBuilder.path( PATH ).path( ID ).build( object.getId() ) );
  }

  @NotNull
  @Override
  protected com.cedarsoft.rest.sample.jaxb.Group.Jaxb createJaxbObject( @NotNull Group object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    com.cedarsoft.rest.sample.jaxb.Group.Jaxb jaxbObject = new com.cedarsoft.rest.sample.jaxb.Group.Jaxb();
    jaxbObject.setId( object.getId() );
    jaxbObject.setDescription( object.getDescription() );
    return jaxbObject;
  }

  @Override
  protected com.cedarsoft.rest.sample.jaxb.Group.Stub createJaxbObjectStub( @NotNull Group object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    com.cedarsoft.rest.sample.jaxb.Group.Stub stub = new com.cedarsoft.rest.sample.jaxb.Group.Stub();
    stub.setId( object.getId() );
    return stub;
  }

}
