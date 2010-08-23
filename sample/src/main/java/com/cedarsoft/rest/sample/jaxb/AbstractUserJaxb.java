package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AbstractUserJaxb extends AbstractJaxbObject {
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
}
