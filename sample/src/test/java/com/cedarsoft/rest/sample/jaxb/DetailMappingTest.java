package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.AbstractMappedJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.sample.Detail;
import org.junit.experimental.theories.*;

public class DetailMappingTest extends AbstractMappedJaxbTest<Detail, com.cedarsoft.rest.sample.jaxb.Detail.Jaxb, com.cedarsoft.rest.sample.jaxb.Detail.Stub> {
  public DetailMappingTest() {
    super( com.cedarsoft.rest.sample.jaxb.Detail.Jaxb.class, com.cedarsoft.rest.sample.jaxb.Detail.Stub.class );
  }

  @Override
  protected JaxbMapping<Detail, com.cedarsoft.rest.sample.jaxb.Detail.Jaxb, com.cedarsoft.rest.sample.jaxb.Detail.Stub> createMapping() {
    return new DetailMapping();
  }

  @DataPoint
  public static Entry<? extends Detail> dataPoint1() {
    Detail object = new Detail( "daID", "text" );
    return AbstractJaxbTest.create( object, DetailMappingTest.class.getResource( "DetailMappingTest.dataPoint1.xml" ), DetailMappingTest.class.getResource( "DetailMappingTest.stub.xml" ) );
  }
}
