package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.AbstractMappedJaxbTest;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.generator.test.BarModel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class BarModelJaxbMappingTest extends AbstractMappedJaxbTest<BarModel, BarModelJaxb> {
  @NotNull
  @Override
  protected JaxbMapping<BarModel, BarModelJaxb> createMapping() {
    return new BarModelJaxbMapping();
  }

  @NotNull
  @Override
  protected BarModel createModel() {
    return new BarModel( "daId", 42, "daString", Arrays.asList( "wildStringList", "2", "3" ), Arrays.asList( "stringList", "4" ), new HashSet<String>( Arrays.asList( "set", "other" ) ) );
  }

  @NotNull
  @Override
  protected Class<BarModelJaxb> getJaxbType() {
    return BarModelJaxb.class;
  }
}
