package com.cedarsoft.jaxb;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface JaxbCollection<T> {
  @NotNull
  @NonNls
  String NS_COLLECTION_SUFFIX = "/list";

  int getStartIndex();

  void setStartIndex( int startIndex );

  int getSize();

  void setSize( int size );
}