package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.JaxbMappingContext;
import com.cedarsoft.rest.generator.test.BarModel;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;

/**
 *
 */
public class BarModelJaxbMapping extends JaxbMapping<BarModel, BarModelJaxb> {
  @Override
  protected void setUris( @NotNull BarModelJaxb object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
    object.setHref( uriBuilder.path( "asdf" ).build() );
  }

  @NotNull
  @Override
  protected BarModelJaxb createJaxbObject( @NotNull BarModel object, @NotNull JaxbMappingContext context ) {
    BarModelJaxb jaxbObject = new BarModelJaxb();

    jaxbObject.setId( object.getId() );

    jaxbObject.setDaInt( object.getDaInt() );
    jaxbObject.setDaInt( object.getDaInt() );
    jaxbObject.setDaString( object.getDaString() );
    jaxbObject.setStringList( object.getStringList() );
    jaxbObject.setWildStringList( object.getWildStringList() );
    jaxbObject.setSet( object.getSet() );

    return jaxbObject;
  }
}
