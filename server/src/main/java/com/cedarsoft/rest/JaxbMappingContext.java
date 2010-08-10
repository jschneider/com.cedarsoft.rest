package com.cedarsoft.rest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.core.UriBuilder;

/**
 *
 */
public class JaxbMappingContext {
  @Nullable
  private final UriBuilder uriBuilder;
  @NotNull
  private final DelegateJaxbMappings delegateJaxbMappings;

  public JaxbMappingContext( @Nullable UriBuilder uriBuilder, @NotNull DelegateJaxbMappings delegateJaxbMappings ) {
    this.uriBuilder = uriBuilder;
    this.delegateJaxbMappings = delegateJaxbMappings;
  }

  @Nullable
  public UriBuilder getUriBuilder() {
    return uriBuilder;
  }

  @NotNull
  public DelegateJaxbMappings getDelegateJaxbMappings() {
    return delegateJaxbMappings;
  }
}
