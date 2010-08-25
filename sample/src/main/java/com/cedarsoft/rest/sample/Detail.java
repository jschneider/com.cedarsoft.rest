package com.cedarsoft.rest.sample;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Detail {
  @NotNull
  @NonNls
  private final String id;
  @NotNull
  @NonNls
  private final String text;

  public Detail( String id, @NotNull String text ) {
    this.id = id;
    this.text = text;
  }

  @NotNull
  @NonNls
  public String getId() {
    return id;
  }

  @NotNull
  public String getText() {
    return text;
  }
}
