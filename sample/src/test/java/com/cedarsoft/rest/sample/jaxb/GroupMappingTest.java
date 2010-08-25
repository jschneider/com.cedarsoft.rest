package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.AbstractMappedJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.sample.Group;
import org.junit.experimental.theories.*;

public class GroupMappingTest extends AbstractMappedJaxbTest<Group, com.cedarsoft.rest.sample.jaxb.Group.Jaxb, com.cedarsoft.rest.sample.jaxb.Group.Stub> {
  public GroupMappingTest() {
    super( com.cedarsoft.rest.sample.jaxb.Group.Jaxb.class, com.cedarsoft.rest.sample.jaxb.Group.Stub.class );
  }

  @Override
  protected JaxbMapping<Group, com.cedarsoft.rest.sample.jaxb.Group.Jaxb, com.cedarsoft.rest.sample.jaxb.Group.Stub> createMapping() {
    return new GroupMapping();
  }

  @DataPoint
  public static Entry<? extends Group> dataPoint1() {
    Group object = new Group( "id", "description" );
    return AbstractJaxbTest.create( object, GroupMappingTest.class.getResource( "GroupMappingTest.dataPoint1.xml" ), GroupMappingTest.class.getResource( "GroupMappingTest.stub.xml" ) );
  }

}
