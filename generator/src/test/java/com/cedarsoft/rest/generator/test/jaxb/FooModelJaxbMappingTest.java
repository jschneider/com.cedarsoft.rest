package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.AbstractMappedJaxbTest;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.generator.test.BarModel;
import com.cedarsoft.rest.generator.test.FooModel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class FooModelJaxbMappingTest extends AbstractMappedJaxbTest<FooModel, FooModelJaxb> {
  @NotNull
  @Override
  protected JaxbMapping<FooModel, FooModelJaxb> createMapping() {
    return new FooModelJaxbMapping( new BarModelJaxbMapping() );
  }

  @NotNull
  @Override
  protected FooModel createModel() {
    return new FooModel( "anid",
                         new BarModel( "singleBar", 41, "daString", Arrays.asList( "wildStringList", "2", "3" ), Arrays.asList( "stringList", "4" ), new HashSet<String>( Arrays.asList( "set", "other" ) ) ),
                         Arrays.asList(
                           new BarModel( "bar0", 42, "daString", Arrays.asList( "wildStringList", "2", "3" ), Arrays.asList( "stringList", "4" ), new HashSet<String>( Arrays.asList( "set", "other" ) ) ),
                           new BarModel( "bar1", 43, "daString", Arrays.asList( "wildStringList", "2", "3" ), Arrays.asList( "stringList", "4" ), new HashSet<String>( Arrays.asList( "set", "other" ) ) )
                         ) );
  }

  @NotNull
  @Override
  protected Class<FooModelJaxb> getJaxbType() {
    return FooModelJaxb.class;
  }
}
