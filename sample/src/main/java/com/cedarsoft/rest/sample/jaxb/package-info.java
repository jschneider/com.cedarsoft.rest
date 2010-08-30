@javax.xml.bind.annotation.XmlSchema(
  xmlns = {
    @javax.xml.bind.annotation.XmlNs( prefix = "cam", namespaceURI = Camera.NS ),
    @javax.xml.bind.annotation.XmlNs( prefix = "cam-s", namespaceURI = Camera.Stub.NS_STUB ),

    @javax.xml.bind.annotation.XmlNs( prefix = "ci", namespaceURI = CameraInfo.NS ),
    @javax.xml.bind.annotation.XmlNs( prefix = "ci-s", namespaceURI = CameraInfo.Stub.NS_STUB ),

    @javax.xml.bind.annotation.XmlNs( prefix = "detail", namespaceURI = Detail.NS ),
    @javax.xml.bind.annotation.XmlNs( prefix = "detail-s", namespaceURI = Detail.Stub.NS_STUB ),

    @javax.xml.bind.annotation.XmlNs( prefix = "email", namespaceURI = Email.NS ),
    @javax.xml.bind.annotation.XmlNs( prefix = "email-stub", namespaceURI = Email.Stub.NS_STUB ),

    @javax.xml.bind.annotation.XmlNs( prefix = "group", namespaceURI = Group.NS ),
    @javax.xml.bind.annotation.XmlNs( prefix = "group-stub", namespaceURI = Group.Stub.NS_STUB ),

    @javax.xml.bind.annotation.XmlNs( prefix = "user", namespaceURI = User.Jaxb.NS ),
    @javax.xml.bind.annotation.XmlNs( prefix = "user-s", namespaceURI = User.Stub.NS_STUB )

  },
  elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED ) package com.cedarsoft.rest.sample.jaxb;
