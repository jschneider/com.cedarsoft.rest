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

  public Group( @NotNull @NonNls String id ) {
    this.id = id;
  }

  @NotNull
  @NonNls
  public String getId() {
    return id;
  }
}
