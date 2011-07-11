package com.cedarsoft.rest.generator.test;


import javax.annotation.Nonnull;

/**
 *
 */
public class Group {
  @Nonnull

  private final String id;
  private final String description;

  public Group( @Nonnull  String id, @Nonnull  String description ) {
    this.id = id;
    this.description = description;
  }

  @Nonnull

  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }
}
