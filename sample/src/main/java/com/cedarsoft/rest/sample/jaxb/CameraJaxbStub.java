package com.cedarsoft.rest.sample.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbStub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@XmlRootElement( name = "camera", namespace = "http://cedarsoft.com/rest/sample/camera/stub" )
@XmlAccessorType( XmlAccessType.FIELD )
public class CameraJaxbStub extends AbstractJaxbStub {
  private CameraInfoJaxbStub cameraInfo;

  public CameraInfoJaxbStub getCameraInfo() {
    return cameraInfo;
  }

  public void setCameraInfo( CameraInfoJaxbStub cameraInfo ) {
    this.cameraInfo = cameraInfo;
  }
}
