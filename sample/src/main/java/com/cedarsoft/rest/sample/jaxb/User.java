package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.JaxbStub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType( name = "abstractUser" )
public abstract class User extends AbstractJaxbObject {
  private String email;
  private String name;

  public String getEmail() {
    return email;
  }

  public void setEmail( String email ) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  @XmlType( name = "user" )
  @XmlRootElement( name = "user", namespace = "http://cedarsoft.com/rest/sample/user" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends User {

    private Group.Stub group;
    @XmlElement( name = "friend" )
    private List<com.cedarsoft.rest.sample.jaxb.User.Stub> friends;

    public Group.Stub getGroup() {
      return group;
    }

    public void setGroup( Group.Stub group ) {
      this.group = group;
    }

    public List<com.cedarsoft.rest.sample.jaxb.User.Stub> getFriends() {
      return friends;
    }

    public void setFriends( List<com.cedarsoft.rest.sample.jaxb.User.Stub> friends ) {
      this.friends = friends;
    }
  }

  @XmlType( name = "userStub" )
  @XmlRootElement( name = "user", namespace = "http://cedarsoft.com/rest/sample/user/stub" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends User implements JaxbStub<User.Jaxb> {
    @Override
    public Class<User.Jaxb> getJaxbType() {
      return User.Jaxb.class;
    }

  }

}
