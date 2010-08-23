package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.JaxbStub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@XmlType( name = "abstractCameraInfo" )
public abstract class CameraInfo extends AbstractJaxbObject {
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

  @XmlType( name = "cameraInfoStub" )
  @XmlRootElement( name = "cameraInfo", namespace = "http://cedarsoft.com/rest/sample/camera-info/stub" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends CameraInfo implements JaxbStub {
  }

  @XmlType( name = "cameraInfo" )
  @XmlRootElement( name = "cameraInfo", namespace = "http://cedarsoft.com/rest/sample/camera-info" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends CameraInfo {
    private long serial;
    private String internalSerial;

    public long getSerial() {
      return serial;
    }

    public void setSerial( long serial ) {
      this.serial = serial;
    }

    public String getInternalSerial() {
      return internalSerial;
    }

    public void setInternalSerial( String internalSerial ) {
      this.internalSerial = internalSerial;
    }
  }
}
