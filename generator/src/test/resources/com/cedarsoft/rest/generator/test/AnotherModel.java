package com.cedarsoft.rest.generator.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AnotherModel {
  private BarModel singleBar;
  private  List<BarModel> theBars = new ArrayList<BarModel>();
  private  List<? extends BarModel> wildcardBars = new ArrayList<BarModel>();
  private  List<? extends String> wildcardStrings ;
  private  List<Integer> integers ;

  public List<? extends String> getWildcardStrings() {
    return wildcardStrings;
  }

  public void setWildcardStrings( List<? extends String> wildcardStrings ) {
    this.wildcardStrings = wildcardStrings;
  }

  public List<Integer> getIntegers() {
    return integers;
  }

  public void setIntegers( List<Integer> integers ) {
    this.integers = integers;
  }

  public void setTheBars( List<BarModel> theBars ) {
    this.theBars = theBars;
  }

  public void setWildcardBars( List<? extends BarModel> wildcardBars ) {
    this.wildcardBars = wildcardBars;
  }

  public void setSingleBar( BarModel singleBar ) {
    this.singleBar = singleBar;
  }

  public BarModel getSingleBar() {
    return singleBar;
  }

  public List<BarModel> getTheBars() {
    return theBars;
  }

  public List<? extends BarModel> getWildcardBars() {
    return wildcardBars;
  }
}
