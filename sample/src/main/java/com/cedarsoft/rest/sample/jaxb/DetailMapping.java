package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.JaxbMappingContext;
import com.cedarsoft.rest.sample.Detail;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;

public class DetailMapping extends JaxbMapping<Detail, com.cedarsoft.rest.sample.jaxb.Detail.Jaxb, com.cedarsoft.rest.sample.jaxb.Detail.Stub> {

  @NotNull
  @NonNls
  public static final String PATH = "details";
  @NotNull
  @NonNls
  public static final String ID = "{id}";

  @Override
  protected void setUris( @NotNull JaxbObject object, @NotNull UriBuilder uriBuilder ) {
    object.setHref( uriBuilder.path( PATH ).path( ID ).build( object.getId() ) );
  }

  @NotNull
  @Override
  protected com.cedarsoft.rest.sample.jaxb.Detail.Jaxb createJaxbObject( @NotNull Detail object, @NotNull JaxbMappingContext context )
    throws URISyntaxException {
    com.cedarsoft.rest.sample.jaxb.Detail.Jaxb jaxbObject = new com.cedarsoft.rest.sample.jaxb.Detail.Jaxb();
    jaxbObject.setText( object.getText() );
    jaxbObject.setId( object.getId() );
    return jaxbObject;
  }

  @Override
  protected com.cedarsoft.rest.sample.jaxb.Detail.Stub createJaxbObjectStub( @NotNull Detail object, @NotNull JaxbMappingContext context )
    throws URISyntaxException {
    com.cedarsoft.rest.sample.jaxb.Detail.Stub stub = new com.cedarsoft.rest.sample.jaxb.Detail.Stub();
    stub.setText( object.getText() );
    stub.setId( object.getId() );
    return stub;
  }

}
