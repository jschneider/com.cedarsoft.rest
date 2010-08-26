package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.JaxbStub;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = "abstractGroup" )
public abstract class Group extends AbstractJaxbObject {
  protected Group() {
  }

  protected Group( @NotNull @NonNls String id ) {
    super( id );
  }

  @XmlType( name = "group" )
  @XmlRootElement( name = "group", namespace = "http://cedarsoft.com/rest/sample/group" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends Group {
    private String description;

    public Jaxb() {
    }

    public Jaxb( @NotNull @NonNls String id ) {
      super( id );
    }

    public String getDescription() {
      return description;
    }

    public void setDescription( String description ) {
      this.description = description;
    }
  }

  @XmlType( name = "groupStub" )
  @XmlRootElement( name = "group", namespace = "http://cedarsoft.com/rest/sample/group/stub" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends Group implements JaxbStub<Group.Jaxb> {
    public Stub() {
    }

    public Stub( @NotNull @NonNls String id ) {
      super( id );
    }

    @Override
    public Class<Group.Jaxb> getJaxbType() {
      return Group.Jaxb.class;
    }

  }

}
