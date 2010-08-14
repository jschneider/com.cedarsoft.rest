package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement( namespace = "http://cedarsoft.com/rest/generator/test/foo-model" )
@XmlAccessorType( XmlAccessType.FIELD )
public class FooModelJaxb extends AbstractJaxbObject {

  private BarModelJaxb singleBar;
  @XmlElementRef
  private List<BarModelJaxb> theBars;

  public BarModelJaxb getSingleBar() {
    return singleBar;
  }

  public void setSingleBar( BarModelJaxb singleBar ) {
    this.singleBar = singleBar;
  }

  public List<BarModelJaxb> getTheBars() {
    return theBars;
  }

  public void setTheBars( List<BarModelJaxb> theBars ) {
    this.theBars = theBars;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FooModelJaxb ) ) return false;

    FooModelJaxb that = ( FooModelJaxb ) o;

    if ( singleBar != null ? !singleBar.equals( that.singleBar ) : that.singleBar != null ) return false;
    if ( theBars != null ? !theBars.equals( that.theBars ) : that.theBars != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = singleBar != null ? singleBar.hashCode() : 0;
    result = 31 * result + ( theBars != null ? theBars.hashCode() : 0 );
    return result;
  }
}
