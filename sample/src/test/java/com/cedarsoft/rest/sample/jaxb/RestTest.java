package com.cedarsoft.rest.sample.jaxb;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.cedarsoft.AssertUtils.assertXMLEquals;
import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RestTest extends JerseyTest {
  /**
   * @noinspection RefusedBequest
   */
  @Override
  protected AppDescriptor configure() {
    return new WebAppDescriptor.Builder()
      .contextListenerClass( GuiceConfig.class )
      .filterClass( GuiceFilter.class )
      .servletPath( "/" )
      .build();
  }

  @Test
  public void testConvert() throws Exception {
    assertXMLEquals( getClass().getResource( "Rest.testUser.xml" ), resource().path( "users/test" ).accept( MediaType.APPLICATION_XML ).get( String.class ) );
    assertXMLEquals( getClass().getResource( "Rest.testUser.xml" ), resource().path( "users/test" ).accept( MediaType.TEXT_XML ).get( String.class ) );

    UserJaxb.Complete testUser = resource().path( "users/test" ).accept( MediaType.APPLICATION_XML ).get( UserJaxb.Complete.class );
    assertNotNull( testUser );
    assertEquals( "test@test.com", testUser.getEmail() );
    assertEquals( 1, testUser.getFriends().size() );
    assertEquals( "Test User", testUser.getName() );
    assertEquals( "http://localhost:9998/users/test@test.com", testUser.getHref().toString() );
    assertEquals( "test@test.com", testUser.getId() );
  }

  @Test
  public void testATest() throws Exception {
    assertNotNull( resource().path( "users/test" ).accept( MediaType.APPLICATION_XML ).get( UserJaxb.Complete.class ) );

    assertNotNull( resource().path( "users/test" ).type( MediaType.APPLICATION_XML ).get( UserJaxb.Complete.class ) );
    assertNotNull( resource().path( "users" ).type( MediaType.APPLICATION_XML ).get( new GenericType<List<UserJaxb.Complete>>() {
    } ) );

    assertXMLEquals( getClass().getResource( "Rest.users.xml" ), resource().path( "users" ).type( MediaType.APPLICATION_XML ).get( String.class ) );
    assertXMLEquals( getClass().getResource( "Rest.testUser.xml" ), resource().path( "users/test" ).type( MediaType.APPLICATION_XML ).get( String.class ) );
  }

  @Test
  public void testJson() throws Exception {
    assertEquals( IOUtils.toString( getClass().getResourceAsStream( "Rest.testUser.json" ) ), resource().path( "users/test" ).accept( MediaType.APPLICATION_JSON ).get( String.class ) );
  }
}
