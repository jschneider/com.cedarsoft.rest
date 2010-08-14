package com.cedarsoft.rest;

import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;

/**
 *
 */
public class JaxbTestUtils {
  private JaxbTestUtils() {
  }

  @NotNull
  public static UriBuilder createTestUriBuilder() {
    return UriBuilder.fromUri( "http://test.running/here" );
  }
}
