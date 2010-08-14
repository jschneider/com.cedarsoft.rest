package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.JaxbMappingContext;
import com.cedarsoft.rest.generator.test.FooModel;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 */
public class FooModelJaxbMapping extends JaxbMapping<FooModel, FooModelJaxb> {
  public FooModelJaxbMapping( @NotNull BarModelJaxbMapping barModelJaxbMapping ) {
    getDelegatesMapping().addMapping( BarModelJaxb.class, barModelJaxbMapping );
  }

  @Override
  protected void setUris( @NotNull FooModelJaxb object, @NotNull UriBuilder uriBuilder ) throws URISyntaxException {
    object.setHref( uriBuilder.path( "daHrefForFoo" ).build() );
  }

  @NotNull
  @Override
  protected FooModelJaxb createJaxbObject( @NotNull FooModel object, @NotNull JaxbMappingContext context ) throws URISyntaxException {
    FooModelJaxb jaxbModel = new FooModelJaxb();
    jaxbModel.setId( object.getId() );

    jaxbModel.setSingleBar( get( BarModelJaxb.class, object.getSingleBar(), context ) );
    jaxbModel.setTheBars( getList( BarModelJaxb.class, object.getTheBars(), context ) );
    return jaxbModel;
  }
}
