package com.cedarsoft.rest;

import com.cedarsoft.jaxb.AbstractJaxbStub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@XmlRootElement( namespace = "test:foo/stub" )
@XmlAccessorType( XmlAccessType.FIELD )
public class FooStub extends AbstractJaxbStub {
  private String daValue = "default";
}
