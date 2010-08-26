package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.UriContext;
import com.cedarsoft.rest.sample.Detail;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;

public class DetailMapping extends JaxbMapping<Detail, com.cedarsoft.rest.sample.jaxb.Detail.Jaxb, com.cedarsoft.rest.sample.jaxb.Detail.Stub> {

  @NotNull
  @NonNls
  public static final String PATH = "details";

  @NotNull
  @Override
  protected UriBuilder getUri( @NotNull JaxbObject object, @NotNull UriContext context ) {
    return context.getUriBuilder().path( PATH ).path( object.getId() );
  }

  @NotNull
  @Override
  protected com.cedarsoft.rest.sample.jaxb.Detail.Jaxb createJaxbObject( @NotNull Detail object ) {
    return new com.cedarsoft.rest.sample.jaxb.Detail.Jaxb( object.getId() );
  }

  @Override
  protected com.cedarsoft.rest.sample.jaxb.Detail.Stub createJaxbStub( @NotNull Detail object ) {
    return new com.cedarsoft.rest.sample.jaxb.Detail.Stub( object.getId() );
  }

  @Override
  protected void copyFieldsToJaxbObject( @NotNull Detail source, @NotNull com.cedarsoft.rest.sample.jaxb.Detail.Jaxb target, @NotNull UriContext context ) {
    target.setText( source.getText() );
  }

  @Override
  protected void copyFieldsToStub( @NotNull Detail source, @NotNull com.cedarsoft.rest.sample.jaxb.Detail.Stub target, @NotNull UriContext context ) {
    target.setText( source.getText() );
  }
}
