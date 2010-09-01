package com.cedarsoft.rest.generator.test;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class Group {
  @NotNull
  @NonNls
  private final String id;
  private final String description;

  public Group( @NotNull @NonNls String id, @NotNull @NonNls String description ) {
    this.id = id;
    this.description = description;
  }

  @NotNull
  @NonNls
  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }
}
