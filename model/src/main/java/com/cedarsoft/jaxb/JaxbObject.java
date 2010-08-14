package com.cedarsoft.jaxb;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 *
 */
public interface JaxbObject {
  @NotNull
  URI getHref();

  @NotNull
  String getId();

  boolean isIdSet();
}