package com.cedarsoft.rest.sample;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Group {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  @NonNls
  private final String description;

  public Group( @NotNull @NonNls String id, @NotNull @NonNls String description ) {
    this.id = id;
    this.description = description;
  }

  @NotNull
  public String getDescription() {
    return description;
  }

  @NotNull
  @NonNls
  public String getId() {
    return id;
  }
}
