package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class BarModelTest extends AbstractJaxbTest {
  @NotNull
  @Override
  protected Class<BarModel> getJaxbType() {
    return BarModel.class;
  }

  @NotNull
  @Override
  protected BarModel createObjectToSerialize()
    throws Exception {
    BarModel object = new BarModel();
    object.setDaInt( 42 );
    object.setDaString( "daString" );
    object.setStringList( Arrays.asList( "stringList", "4" ) );
    object.setWildStringList( Arrays.asList( "wildStringList", "2", "3" ) );
    object.setSet( new HashSet<String>( Arrays.asList( "set", "other" ) ) );
    return object;
  }

}
