package com.cedarsoft.rest;

import org.jetbrains.annotations.Nullable;

import javax.ws.rs.core.UriBuilder;

/**
 *
 */
public class JaxbMappingContext {
  @Nullable
  private final UriBuilder uriBuilder;

  public JaxbMappingContext( @Nullable UriBuilder uriBuilder ) {
    this.uriBuilder = uriBuilder;
  }
}
