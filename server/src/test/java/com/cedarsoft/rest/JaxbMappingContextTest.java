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
    context = new JaxbMappingContext( JaxbTestUtils.createTestUriBuilder(), new DelegateJaxbMappings() );
  }

  @Test
  public void testWithUriBuilder() throws Exception {
    assertNotNull( context );
    assertEquals( "http://test.running/here/dir", context.getUriBuilder().path( "dir" ).build().toString() );
    assertEquals( "http://test.running/here/dir2", context.getUriBuilder().path( "dir2" ).build().toString() );
    assertEquals( "http://test.running/here/dir3", context.getUriBuilder().path( "dir3" ).build().toString() );
  }

  @Test
  public void testSub() throws Exception {
    UriBuilder uriBuilder = context.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );

    JaxbMappingContext newContext = new JaxbMappingContext( uriBuilder, context.getDelegateJaxbMappings() );
    assertEquals( "http://test.running/here/dir/sub", newContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testSubApi() throws Exception {
    UriBuilder uriBuilder = context.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );

    JaxbMappingContext newContext = context.create( uriBuilder );
    assertEquals( "http://test.running/here/dir/sub", newContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testSubClone() throws Exception {
    UriBuilder uriBuilder = context.getUriBuilder().path( "dir" );
    assertEquals( "http://test.running/here/dir", uriBuilder.build().toString() );
    JaxbMappingContext newContext = context.create( uriBuilder );

    assertEquals( "http://test.running/here/dir/another", uriBuilder.path( "another" ).build().toString() );

    assertEquals( "http://test.running/here/dir/sub", newContext.getUriBuilder().path( "sub" ).build().toString() );
    assertEquals( "http://test.running/here/dir/sub1", newContext.getUriBuilder().path( "sub1" ).build().toString() );
  }

  @Test
  public void testUriBuilder() throws Exception {
    UriBuilder uriBuilder = context.getUriBuilder().path( "users" ).path( "{id}" );
    assertEquals( "http://test.running/here/users/info@cedarsoft.com", uriBuilder.build( "info@cedarsoft.com" ).toString() );

    JaxbMappingContext newContext = context.create( UriBuilder.fromUri( uriBuilder.build( "info@cedarsoft.com" ) ) );
    
    assertEquals( "http://test.running/here/users/info@cedarsoft.com/details/7", newContext.getUriBuilder().path( "details" ).path( "{id}" ).build( "7" ).toString() );

  }
}
