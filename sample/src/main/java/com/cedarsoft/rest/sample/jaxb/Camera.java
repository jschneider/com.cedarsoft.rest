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
public abstract class Camera extends AbstractJaxbObject {
  @XmlType(name = "camera")
  @XmlRootElement( name = "camera", namespace = "http://cedarsoft.com/rest/sample/camera" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Jaxb extends Camera {
    private CameraInfo.Jaxb cameraInfo;
    private String description;
    private User.Stub owner;

    public CameraInfo.Jaxb getCameraInfo() {
      return cameraInfo;
    }

    public void setCameraInfo( CameraInfo.Jaxb cameraInfo ) {
      this.cameraInfo = cameraInfo;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription( String description ) {
      this.description = description;
    }

    public User.Stub getOwner() {
      return owner;
    }

    public void setOwner( User.Stub owner ) {
      this.owner = owner;
    }
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  @XmlType(name = "cameraStub")
  @XmlRootElement( name = "camera", namespace = "http://cedarsoft.com/rest/sample/camera/stub" )
  @XmlAccessorType( XmlAccessType.FIELD )
  public static class Stub extends Camera implements JaxbStub {
    private CameraInfo.Stub cameraInfo;

    public CameraInfo.Stub getCameraInfo() {
      return cameraInfo;
    }

    public void setCameraInfo( CameraInfo.Stub cameraInfo ) {
      this.cameraInfo = cameraInfo;
    }
  }
}
