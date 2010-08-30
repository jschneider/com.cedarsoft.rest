package com.cedarsoft.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 * @param <T> the JaxbStub type
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlTransient
public abstract class AbstractJaxbCollection<T> implements JaxbCollection<T> {
  @XmlAttribute
  private int startIndex;
  @XmlAttribute
  private int size;

  protected AbstractJaxbCollection() {
  }

  protected AbstractJaxbCollection( int startIndex, int size ) {
    this.startIndex = startIndex;
    this.size = size;
  }

  @Override
  public int getStartIndex() {
    return startIndex;
  }

  @Override
  public void setStartIndex( int startIndex ) {
    this.startIndex = startIndex;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public void setSize( int size ) {
    this.size = size;
  }

}
