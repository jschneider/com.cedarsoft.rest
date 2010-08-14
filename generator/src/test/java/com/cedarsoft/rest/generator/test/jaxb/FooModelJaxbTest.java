package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.JaxbTestUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FooModelJaxbTest
  extends AbstractJaxbTest<FooModelJaxb> {


  @Override
  protected Class<FooModelJaxb> getJaxbType() {
    return FooModelJaxb.class;
  }

  @Override
  public FooModelJaxb createObjectToSerialize()
    throws Exception {
    FooModelJaxb object = new FooModelJaxb();
    object.setSingleBar( new BarModelJaxb() );
    object.setTheBars( Arrays.asList( new BarModelJaxb(), new BarModelJaxb() ) );

    object.setId( "daId" );
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );

    return object;
  }
}
