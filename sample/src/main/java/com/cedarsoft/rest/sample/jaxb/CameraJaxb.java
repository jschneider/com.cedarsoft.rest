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
public abstract class CameraJaxb extends AbstractJaxbObject {
  @XmlType(name = "camera")
  @XmlRootElement( name = "camera", namespace = "http://cedarsoft.com/rest/sample/camera" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Complete extends CameraJaxb {
    private CameraInfoJaxb.Complete cameraInfo;
    private String description;
    private UserJaxb.Stub owner;

    public CameraInfoJaxb.Complete getCameraInfo() {
      return cameraInfo;
    }

    public void setCameraInfo( CameraInfoJaxb.Complete cameraInfo ) {
      this.cameraInfo = cameraInfo;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription( String description ) {
      this.description = description;
    }

    public UserJaxb.Stub getOwner() {
      return owner;
    }

    public void setOwner( UserJaxb.Stub owner ) {
      this.owner = owner;
    }
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  @XmlType(name = "cameraStub")
  @XmlRootElement( name = "camera", namespace = "http://cedarsoft.com/rest/sample/camera/stub" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends CameraJaxb implements JaxbStub {
    private CameraInfoJaxb.Stub cameraInfo;

    public CameraInfoJaxb.Stub getCameraInfo() {
      return cameraInfo;
    }

    public void setCameraInfo( CameraInfoJaxb.Stub cameraInfo ) {
      this.cameraInfo = cameraInfo;
    }
  }
}
