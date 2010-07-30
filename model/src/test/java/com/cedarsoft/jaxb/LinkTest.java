package com.cedarsoft.jaxb;

import org.junit.*;
import org.junit.rules.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 *
 */
public class LinkTest {
  @Test
  public void testIt() throws URISyntaxException {
    Link link = new Link( new URI( "http://www.test.de/asdf" ), Link.SELF );
    assertNotNull( link.getHref() );
    assertEquals( Link.SELF, link.getType() );
  }
}
