package com.cedarsoft.rest;

import com.cedarsoft.jaxb.AbstractJaxbObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DelegateJaxbMappings {
  @NotNull
  private final Map<Class<? extends AbstractJaxbObject>, JaxbMapping<?, ?>> delegates = new HashMap<Class<? extends AbstractJaxbObject>, JaxbMapping<?, ?>>();

  public <T, J extends AbstractJaxbObject> void addMapping( @NotNull Class<J> jaxbObjectType, @NotNull JaxbMapping<T, J> mapping ) {
    delegates.put( jaxbObjectType, mapping );
  }

  @NotNull
  public <T, J extends AbstractJaxbObject> JaxbMapping<T, J> getMapping( @NotNull Class<J> jaxbObjectType ) {
    JaxbMapping<?, ?> resolved = delegates.get( jaxbObjectType );
    if ( resolved == null ) {
      throw new IllegalArgumentException( "No mapping found for " + jaxbObjectType.getName() );
    }
    return ( JaxbMapping<T, J> ) resolved;
  }

}
