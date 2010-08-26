package com.cedarsoft.rest;

import org.junit.*;

import javax.ws.rs.core.UriBuilder;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JaxbMappingContextTest {
  private JaxbMappingContext context;

  @Before
  public void setUp() throws Exception {
    context = new JaxbMappingContext( JaxbTestUtils.createTestUriBuilder(), JaxbTestUtils.createTestUriBuilder(), new DelegateJaxbMappings() );
  }

  @Test
  public void testWithUriBuilder() throws Exception {
    assertNotNull( context );
    assertEquals( "http://test.running/here/dir", context.uriContext.getUriBuilder().path( "dir" ).build().toString() );
    assertEquals( "http://test.running/here/dir2", context.uriContext.getUriBuilder().path( "dir2" ).build().toString() );
    assertEquals( "http://test.running/here/dir3", context.uriContext.getUriBuilder().path( "dir3" ).build().toString() );
  }

  @Test
  public void testSub() throws Exception {
    UriBuilder uriBuilder = context.uriContext.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );

    JaxbMappingContext newContext = new JaxbMappingContext( context.uriContext.getBaseUriBuilder(), uriBuilder, context.getDelegateJaxbMappings() );
    assertEquals( "http://test.running/here/dir/sub", newContext.uriContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.uriContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testSubApi() throws Exception {
    UriBuilder uriBuilder = context.uriContext.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );

    JaxbMappingContext newContext = context.create( uriBuilder );
    assertEquals( "http://test.running/here/dir/sub", newContext.uriContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.uriContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testSubClone() throws Exception {
    UriBuilder uriBuilder = context.uriContext.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );
    JaxbMappingContext newContext = context.create( uriBuilder );

    assertEquals( "http://test.running/here/dir/another", uriBuilder.path( "another" ).build().toString() );

    assertEquals( "http://test.running/here/dir/sub", newContext.uriContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.uriContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testUriBuilder() throws Exception {
    UriBuilder uriBuilder = context.uriContext.getUriBuilder().path( "users" ).path( "{id}" );
    assertEquals( "http://test.running/here/users/info@cedarsoft.com", uriBuilder.build( "info@cedarsoft.com" ).toString() );

    JaxbMappingContext newContext = context.create( UriBuilder.fromUri( uriBuilder.build( "info@cedarsoft.com" ) ) );

    assertEquals( "http://test.running/here/users/info@cedarsoft.com/details/7", newContext.uriContext.getUriBuilder().path( "details" ).path( "{id}" ).build( "7" ).toString() );
  }

  @Test
  public void testSubSubApi() throws Exception {
    assertEquals( "http://test.running/here", context.uriContext.getUri().toString() );

    JaxbMappingContext contextUser = context.create( context.uriContext.getUriBuilder().path( "users" ).path( "info@cedarsoft.com" ) );

  }

  @Test
  public void testSubSub() throws Exception {
    assertEquals( "http://test.running/here", context.uriContext.getUri().toString() );

    //Within users
    JaxbMappingContext contextUser = context.create( context.uriContext.getUriBuilder().path( "users" ).path( "info@cedarsoft.com" ) );
    assertEquals( "http://test.running/here/users/info@cedarsoft.com", contextUser.uriContext.getUri().toString() );

    //Within user details
    JaxbMappingContext contextDetail = contextUser.create( contextUser.uriContext.getUriBuilder().path( "details" ).path( "1" ) );
    assertEquals( "http://test.running/here/users/info@cedarsoft.com/details/1", contextDetail.uriContext.getUri().toString() );

    //Now the group...
    JaxbMappingContext contextGroup = contextUser.create( contextUser.uriContext.getBaseUriBuilder().path( "groups" ).path( "nobody" ) );
    assertEquals( "http://test.running/here/groups/nobody", contextGroup.uriContext.getUri().toString() );
  }
}
