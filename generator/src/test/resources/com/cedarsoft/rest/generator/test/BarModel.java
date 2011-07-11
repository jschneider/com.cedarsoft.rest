package com.cedarsoft.rest.generator.test;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class BarModel {
  private final String id;

  private final int daInt;
  private final String daString;

  private final List<String> stringList = new ArrayList<String>();
  private List<? extends String> wildStringList;
  private Set<? extends String> set = new HashSet<String>();

  public BarModel( String id, int daInt, String daString, List<? extends String> wildStringList, List<? extends String> stringList, @Nonnull Set<? extends String> set ) {
    this.id = id;
    this.daInt = daInt;
    this.daString = daString;
    this.wildStringList = wildStringList;
    this.stringList.addAll( stringList );
    this.set = set;
  }

  public String getId() {
    return id;
  }

  public int getDaInt() {
    return daInt;
  }

  public Set<? extends String> getSet() {
    return Collections.unmodifiableSet( set );
  }

  public String getDaString() {
    return daString;
  }

  public List<String> getStringList() {
    return Collections.unmodifiableList( stringList );
  }

  public List<? extends String> getWildStringList() {
    return Collections.unmodifiableList( wildStringList );
  }
}
