package com.cedarsoft.rest;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Entry<T> {
  @NotNull
  private final T object;
  @NotNull
  @NonNls
  private final byte[] expected;

  public Entry( @NotNull T object, @NotNull @NonNls byte[] expected ) {
    this.object = object;
    this.expected = expected;
  }

  @NotNull
  public byte[] getExpected() {
    return expected;
  }

  @NotNull
  public T getObject() {
    return object;
  }
}
