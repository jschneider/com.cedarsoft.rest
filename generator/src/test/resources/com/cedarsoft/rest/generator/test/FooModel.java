package com.cedarsoft.rest.generator.test;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class FooModel {
  private final String id;

  private BarModel singleBar;

  private final List<BarModel> theBars = new ArrayList<BarModel>();

  public FooModel( String id, BarModel singleBar, @Nonnull Collection<? extends BarModel> theBars ) {
    this.id = id;
    this.singleBar = singleBar;
    this.theBars.addAll( theBars );
  }

  public List<? extends BarModel> getTheBars() {
    return Collections.unmodifiableList( theBars );
  }

  public String getId() {
    return id;
  }

  public BarModel getSingleBar() {
    return singleBar;
  }

  public void setSingleBar( BarModel singleBar ) {
    this.singleBar = singleBar;
  }
}
