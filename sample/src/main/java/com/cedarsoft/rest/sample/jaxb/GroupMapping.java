package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.UriContext;
import com.cedarsoft.rest.sample.Group;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;

public class GroupMapping extends JaxbMapping<Group, com.cedarsoft.rest.sample.jaxb.Group.Jaxb, com.cedarsoft.rest.sample.jaxb.Group.Stub> {

  public static final String PATH = "groups";

  @NotNull
  @Override
  protected UriBuilder getUri( @NotNull JaxbObject object, @NotNull UriContext context ) {
    return context.getBaseUriBuilder().path( PATH ).path( object.getId() );
  }

  @NotNull
  @Override
  protected com.cedarsoft.rest.sample.jaxb.Group.Jaxb createJaxbObject( @NotNull Group object ) {
    return new com.cedarsoft.rest.sample.jaxb.Group.Jaxb( object.getId() );
  }

  @Override
  protected com.cedarsoft.rest.sample.jaxb.Group.Stub createJaxbStub( @NotNull Group object ) {
    return new com.cedarsoft.rest.sample.jaxb.Group.Stub( object.getId() );
  }

  @Override
  protected void copyFieldsToJaxbObject( @NotNull Group source, @NotNull com.cedarsoft.rest.sample.jaxb.Group.Jaxb target, @NotNull UriContext context ) {
    target.setDescription( source.getDescription() );
  }

  @Override
  protected void copyFieldsToStub( @NotNull Group source, @NotNull com.cedarsoft.rest.sample.jaxb.Group.Stub target, @NotNull UriContext context ) {
  }
}
