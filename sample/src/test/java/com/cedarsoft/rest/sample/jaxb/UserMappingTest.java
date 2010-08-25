package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.AbstractMappedJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.sample.Group;
import com.cedarsoft.rest.sample.User;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.theories.*;

public class UserMappingTest extends AbstractMappedJaxbTest<User, com.cedarsoft.rest.sample.jaxb.User.Jaxb, com.cedarsoft.rest.sample.jaxb.User.Stub> {
  public UserMappingTest() {
    super( com.cedarsoft.rest.sample.jaxb.User.Jaxb.class, com.cedarsoft.rest.sample.jaxb.User.Stub.class );
  }

  @NotNull
  @Override
  protected JaxbMapping<User, com.cedarsoft.rest.sample.jaxb.User.Jaxb, com.cedarsoft.rest.sample.jaxb.User.Stub> createMapping() {
    return new UserMapping( new GroupMapping(), new DetailMapping() );
  }

  @DataPoint
  public static Entry<? extends User> dataPoint1() {
    User object = new User( "email", "name", new Group( "NOBODY", "A nobody group" ) );
    return AbstractJaxbTest.create( object, UserMappingTest.class.getResource( "UserMappingTest.dataPoint1.xml" ), UserMappingTest.class.getResource( "UserMappingTest.stub.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User> entry1() {
    User user = new User( "info@cedarsoft.com", "Johannes Schneider" );
    user.addFriend( new User( "markus@mustermann.de", "Markus Mustermann" ) );
    user.addFriend( new User( "markus2@mustermann.de", "Markus2 Mustermann" ) );
    user.addFriend( new User( "markus3@mustermann.de", "Markus3 Mustermann" ) );

    user.addDetail( new com.cedarsoft.rest.sample.Detail( "1", "the first detail" ) );
    user.addDetail( new com.cedarsoft.rest.sample.Detail( "2", "the second detail" ) );

    return create( user, UserJaxbTest.class.getResource( "UserMappingTest.recursive.xml" ), UserJaxbTest.class.getResource( "UserMappingTest.recursive.stub.xml" ) );
  }

  @DataPoint
  public static Entry<? extends User> recursive() {
    User user = new User( "info@cedarsoft.com", "Johannes Schneider" );
    User user1 = new User( "markus@mustermann.de", "Markus Mustermann" );
    user.addFriend( user1 );
    user.addFriend( new User( "markus2@mustermann.de", "Markus2 Mustermann" ) );
    user.addFriend( new User( "markus3@mustermann.de", "Markus3 Mustermann" ) );

    user.addDetail( new com.cedarsoft.rest.sample.Detail( "1", "the first detail" ) );
    user.addDetail( new com.cedarsoft.rest.sample.Detail( "2", "the second detail" ) );
    
    user1.addFriend( user );

    return create( user, UserJaxbTest.class.getResource( "UserMappingTest.recursive.xml" ), UserJaxbTest.class.getResource( "UserMappingTest.recursive.stub.xml" ) );
  }
}
