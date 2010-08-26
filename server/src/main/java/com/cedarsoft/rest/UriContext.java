package com.cedarsoft.rest;

import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Represents the URI context.
 * <p/>
 * There is a local context ({@link #getUriBuilder()}) which represents the current position.
 * And there is access to the base uri using {@link #getBaseUriBuilder()}.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UriContext {
  @NotNull
  private final UriBuilder baseUriBuilder;
  @NotNull
  private final UriBuilder uriBuilder;

  public UriContext( @NotNull UriBuilder baseUriBuilder, @NotNull UriBuilder uriBuilder ) {
    this.baseUriBuilder = baseUriBuilder.clone();
    this.uriBuilder = uriBuilder.clone();
  }

  /**
   * Returns a clone of the base uri builder
   *
   * @return the base uri builder
   */
  @NotNull
  public UriBuilder getBaseUriBuilder() {
    return baseUriBuilder.clone();
  }

  /**
   * Returns the uri builder for the local context
   *
   * @return the uri builder
   */
  @NotNull
  public UriBuilder getUriBuilder() {
    return uriBuilder.clone();
  }

  /**
   * Returns the URI
   *
   * @return the URI
   */
  @NotNull
  public URI getUri() {
    return uriBuilder.build();
  }

  @NotNull
  public UriContext create( @NotNull UriBuilder newUriBuilder ) {
    return new UriContext( baseUriBuilder, newUriBuilder );
  }
}
