package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.JaxbStub;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = "abstractDetail" )
public abstract class Detail extends AbstractJaxbObject {
  private String text;

  protected Detail() {
  }

  protected Detail( @NotNull @NonNls String id ) {
    super( id );
  }

  public String getText() {
    return text;
  }

  public void setText( String text ) {
    this.text = text;
  }

  @XmlType( name = "detail" )
  @XmlRootElement( name = "detail", namespace = "http://cedarsoft.com/rest/sample/detail" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends Detail {
    public Jaxb() {
    }

    public Jaxb( @NotNull @NonNls String id ) {
      super( id );
    }
  }

  @XmlType( name = "detailStub" )
  @XmlRootElement( name = "detail", namespace = "http://cedarsoft.com/rest/sample/detail/stub" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends Detail implements JaxbStub<Detail.Jaxb> {
    public Stub() {
    }

    public Stub( @NotNull @NonNls String id ) {
      super( id );
    }

    @NotNull
    @Override
    public Class<Detail.Jaxb> getJaxbType() {
      return Detail.Jaxb.class;
    }

  }

}
