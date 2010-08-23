package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AbstractCameraInfoJaxb extends AbstractJaxbObject {
  private String model;
  private String make;


  public String getModel() {
    return model;
  }

  public void setModel( String model ) {
    this.model = model;
  }

  public String getMake() {
    return make;
  }

  public void setMake( String make ) {
    this.make = make;
  }
}
