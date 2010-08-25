package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbTestUtils;
import com.cedarsoft.rest.SimpleJaxbTest;
import org.junit.experimental.theories.*;

public class DetailJaxbTest extends SimpleJaxbTest<Detail.Jaxb, Detail.Stub> {
  public DetailJaxbTest() {
    super( Detail.Jaxb.class, Detail.Stub.class );
  }

  @DataPoint
  public static Entry<? extends Detail.Jaxb> dataPoint1() {
    Detail.Jaxb object = new Detail.Jaxb();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setText( "text" );
    object.setId( "daid" );
    return AbstractJaxbTest.create( object, DetailJaxbTest.class.getResource( "DetailJaxbTest.dataPoint1.xml" ) );
  }

  @DataPoint
  public static Entry<? extends Detail.Stub> stub() {
    Detail.Stub object = new Detail.Stub();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setText( "text" );
    object.setId( "daid" );
    return AbstractJaxbTest.create( object, DetailJaxbTest.class.getResource( "DetailJaxbTest.stub.xml" ) );
  }

}
