package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbTestUtils;
import com.cedarsoft.rest.SimpleJaxbTest;
import org.junit.experimental.theories.*;

public class GroupJaxbTest extends SimpleJaxbTest<Group.Jaxb, Group.Stub> {
  public GroupJaxbTest() {
    super( Group.Jaxb.class, Group.Stub.class );
  }

  @DataPoint
  public static Entry<? extends Group.Jaxb> dataPoint1() {
    Group.Jaxb object = new Group.Jaxb();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setId( "id" );
    object.setDescription( "description" );
    return AbstractJaxbTest.create( object, GroupJaxbTest.class.getResource( "GroupJaxbTest.dataPoint1.xml" ) );
  }

  @DataPoint
  public static Entry<? extends Group.Stub> stub() {
    Group.Stub object = new Group.Stub();
    object.setHref( JaxbTestUtils.createTestUriBuilder().build() );
    object.setId( "id" );
    return AbstractJaxbTest.create( object, GroupJaxbTest.class.getResource( "GroupJaxbTest.stub.xml" ) );
  }
}
