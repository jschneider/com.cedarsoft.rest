/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.rest.generator;

import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.NamingSupport;
import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.FieldTypeInformation;
import com.cedarsoft.codegen.model.FieldWithInitializationInfo;
import com.cedarsoft.id.NameSpaceSupport;
import com.cedarsoft.jaxb.AbstractJaxbObject;
import com.cedarsoft.jaxb.AbstractJaxbStub;
import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.rest.JaxbMapping;
import com.cedarsoft.rest.JaxbMappingContext;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class Generator extends AbstractGenerator<JaxbObjectGenerator.StubDecisionCallback> {
  @NonNls
  public static final String ID = "id";
  @NotNull
  @NonNls
  public static final String METHOD_NAME_GET_STUB = "getStub";
  @NotNull
  @NonNls
  public static final String MAPPING_SUFFIX = "Mapping";
  @NonNls
  public static final String METHOD_NAME_SET_ID = "setId";
  @NonNls
  public static final String METHOD_NAME_GET_ID = "getId";
  @NonNls
  public static final String METHOD_NAME_SET_URIS = "setUris";
  @NonNls
  public static final String METHOD_NAME_PATH = "path";
  @NonNls
  public static final String METHOD_NAME_BUILD = "build";
  @NonNls
  public static final String ARG_PLACEHOLDER_ID = "{id}";
  @NonNls
  public static final String METHOD_NAME_SET_HREF = "setHref";
  @NonNls
  public static final String NAME = "name";
  @NonNls
  public static final String NAMESPACE = "namespace";
  @NonNls
  public static final String VALUE = "value";
  @NonNls
  public static final String OBJECT = "object";
  @NonNls
  public static final String URI_BUILDER = "uriBuilder";
  @NonNls
  public static final String CONTEXT = "context";
  @NonNls
  public static final String JAXB_OBJECT = "jaxbObject";
  @NonNls
  public static final String METHOD_NAME_CREATE_JAXB_OBJECT = "createJaxbObject";
  @NonNls
  public static final String METHOD_NAME_CREATE_JAXB_STUB = "createJaxbObjectStub";
  @NonNls
  public static final String STUB_NAMESPACE_SUFFIX = "/stub";

  public Generator( @NotNull CodeGenerator<JaxbObjectGenerator.StubDecisionCallback> codeGenerator, @NotNull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  public void generate() throws JClassAlreadyExistsException {
    JDefinedClass jaxbObject = createJaxbObject();
    JDefinedClass jaxbStub = createJaxbStub();

    createJaxbMapping( jaxbObject, jaxbStub );
  }

  private void createJaxbMapping( @NotNull JClass jaxbObject, @NotNull JClass jaxbStub ) throws JClassAlreadyExistsException {
    JClass objectType = codeGenerator.ref( descriptor.getQualifiedName() );
    JClass superType = codeGenerator.ref( JaxbMapping.class ).narrow( objectType ).narrow( jaxbObject ).narrow( jaxbStub );

    JDefinedClass mappingClass = codeGenerator.getModel()._class( getJaxbMappingTypeName() )._extends( superType );

    createHrefMethod( mappingClass );
    createCreateJaxbObjectMethod( mappingClass, jaxbObject );
    createCreateJaxbStubMethod( mappingClass, jaxbStub );
  }

  private void createCreateJaxbObjectMethod( @NotNull JDefinedClass mappingClass, @NotNull JClass jaxbObject ) {
    JMethod method = mappingClass.method( JMod.PROTECTED, jaxbObject, METHOD_NAME_CREATE_JAXB_OBJECT );
    JVar object = method.param( codeGenerator.ref( descriptor.getQualifiedName() ), OBJECT );
    JVar context = method.param( codeGenerator.ref( JaxbMappingContext.class ), CONTEXT );
    method.annotate( Override.class );

    JVar jaxbObjectInstance = method.body().decl( jaxbObject, JAXB_OBJECT, JExpr._new( jaxbObject ) );

    addFieldCopyOperations( mappingClass, object, jaxbObjectInstance, jaxbObject, context, method.body(), false );
    method.body()._return( jaxbObjectInstance );
  }

  private void createCreateJaxbStubMethod( @NotNull JDefinedClass mappingClass, @NotNull JClass jaxbStub ) {
    JMethod method = mappingClass.method( JMod.PROTECTED, jaxbStub, METHOD_NAME_CREATE_JAXB_STUB );
    JVar object = method.param( codeGenerator.ref( descriptor.getQualifiedName() ), OBJECT );
    JVar context = method.param( codeGenerator.ref( JaxbMappingContext.class ), CONTEXT );
    method.annotate( Override.class );

    JVar jaxbObjectInstance = method.body().decl( jaxbStub, JAXB_OBJECT, JExpr._new( jaxbStub ) );

    addFieldCopyOperations( mappingClass, object, jaxbObjectInstance, jaxbStub, context, method.body(), true );
    method.body()._return( jaxbObjectInstance );
  }

  private void addFieldCopyOperations( @NotNull JDefinedClass mappingClass, @NotNull JExpression object, @NotNull JExpression jaxbObjectInstance, @NotNull JClass jaxbObject, @NotNull JExpression context, @NotNull JBlock block, boolean isStub ) {
    Collection<JStatement> statements = new ArrayList<JStatement>();

    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldInfos() ) {
      JInvocation getterInvocation = object.invoke( fieldInfo.getGetterDeclaration().getSimpleName() );

      if ( isStub && skipInStub( fieldInfo ) ) {
        continue;
      }

      JInvocation value;
      if ( isProbablyOwnType( fieldInfo.getType() ) ) {
        JClass fieldJaxbType = getJaxbType( fieldInfo );
        value = JExpr.invoke( METHOD_NAME_GET_STUB )
          .arg( fieldJaxbType.dotclass() )
          .arg( getterInvocation )
          .arg( context )
          ;

        ensureDelegateAvailable( mappingClass, fieldJaxbType );

      } else {
        value = getterInvocation;
      }
      statements.add( jaxbObjectInstance.invoke( NamingSupport.createSetter( fieldInfo.getSimpleName() ) ).arg( value ) );
    }

    for ( JStatement statement : statements ) {
      block.add( statement );
    }
  }

  @NotNull
  private JClass getJaxbType( @NotNull FieldTypeInformation fieldInfo ) {
    if ( TypeUtils.isCollectionType( fieldInfo.getType() ) ) {
      return codeGenerator.ref( getJaxbTypeName( TypeUtils.getErasure( TypeUtils.getCollectionParam( fieldInfo.getType() ) ) ) );
    } else {
      return codeGenerator.ref( getJaxbTypeName( TypeUtils.getErasure( fieldInfo.getType() ) ) );
    }
  }

  private void ensureDelegateAvailable( @NotNull JDefinedClass mappingClass, @NotNull JClass jaxbObject ) {
    JMethod constructor = getOrCreateConstructor( mappingClass );

    String paramName = NamingSupport.createVarName( jaxbObject.name() + MAPPING_SUFFIX );

    JClass mappingType = codeGenerator.ref( jaxbObject.fullName() + MAPPING_SUFFIX );

    //Check whether the mapping still exists
    for ( JVar param : constructor.listParams() ) {
      if ( param.type().equals( mappingType ) ) {
        return;
      }
    }

    //It does not exist, therefore let us add the serializer and map it
    JVar param = constructor.param( mappingType, paramName );

    constructor.body().add(
      JExpr.invoke( "getDelegatesMapping" ).invoke( "addMapping" ).arg( jaxbObject.dotclass() ).arg( param )
    );
  }

  @NotNull
  private JMethod getOrCreateConstructor( @NotNull JDefinedClass mappingClass ) {
    //If no constructor exists, create one
    if ( !mappingClass.constructors().hasNext() ) {
      mappingClass.constructor( JMod.PUBLIC );
    }

    return ( JMethod ) mappingClass.constructors().next();
  }

  private void createHrefMethod( @NotNull JDefinedClass mappingClass ) {
    JMethod method = mappingClass.method( JMod.PROTECTED, Void.TYPE, METHOD_NAME_SET_URIS );
    JVar object = method.param( JaxbObject.class, OBJECT );
    JVar uriBuilder = method.param( UriBuilder.class, URI_BUILDER );
    method.annotate( Override.class );

    JInvocation uriBuilderInvocation = uriBuilder.invoke( METHOD_NAME_PATH )
      .arg( NamingSupport.createXmlElementName( getDescriptor().getClassDeclaration().getSimpleName() ) )
      .invoke( METHOD_NAME_PATH )
      .arg( ARG_PLACEHOLDER_ID )
      .invoke( METHOD_NAME_BUILD )
      .arg( object.invoke( METHOD_NAME_GET_ID ) );

    method.body().add( object.invoke( METHOD_NAME_SET_HREF ).arg( uriBuilderInvocation ) );
  }

  @NotNull
  @NonNls
  protected String getJaxbMappingTypeName() {
    String fqn = descriptor.getQualifiedName();
    return getJaxbMappingTypeName( fqn );
  }

  protected String getJaxbMappingTypeName( @NotNull @NonNls String modelClassName ) {
    return insertSubPackage( modelClassName, JAXB_SUB_PACKAGE ) + JAXB_SUFFIX + MAPPING_SUFFIX;
  }

  @NotNull
  protected JDefinedClass createJaxbObject() throws JClassAlreadyExistsException {
    JDefinedClass jaxbObject = codeGenerator.getModel()._class( getJaxbObjectName() )._extends( AbstractJaxbObject.class );
    jaxbObject.annotate( XmlRootElement.class )
      .param( NAME, NamingSupport.createVarName( descriptor.getClassDeclaration().getSimpleName() ) )
      .param( NAMESPACE, NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) );
    jaxbObject.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );

    addFields( jaxbObject, false );

    return jaxbObject;
  }

  @NotNull
  protected JDefinedClass createJaxbStub() throws JClassAlreadyExistsException {
    JDefinedClass jaxbStub = codeGenerator.getModel()._class( getJaxbStubName() )._extends( AbstractJaxbStub.class );
    jaxbStub.annotate( XmlRootElement.class )
      .param( NAME, NamingSupport.createVarName( descriptor.getClassDeclaration().getSimpleName() ) )
      .param( NAMESPACE, NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) + STUB_NAMESPACE_SUFFIX );
    jaxbStub.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );


    addFields( jaxbStub, true );

    return jaxbStub;
  }

  /**
   * Adds all fields
   *
   * @param jaxbObject the jaxb class
   */
  private void addFields( @NotNull JDefinedClass jaxbObject, boolean isStub ) {
    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldInfos() ) {
      //Skip the id, since it is defined in the super class
      if ( fieldInfo.getSimpleName().equals( ID ) ) {
        continue;
      }

      if ( isStub && skipInStub( fieldInfo ) ) {
        continue;
      }

      JClass fieldType = getJaxbModelType( fieldInfo.getType(), isStub );
      JFieldVar field = addField( jaxbObject, fieldType, fieldInfo );

      if ( TypeUtils.isCollectionType( fieldInfo.getType() ) ) {
        JAnnotationUse annotation = field.annotate( XmlElement.class );
        annotation.param( "name", NamingSupport.createSingular( field.name() ) );
      }

      addGetter( jaxbObject, fieldType, fieldInfo, field );
      addSetter( jaxbObject, fieldType, fieldInfo, field );
    }
  }

  private boolean skipInStub( @NotNull FieldWithInitializationInfo fieldInfo ) {
    return codeGenerator.getDecisionCallback().skipInStub( fieldInfo );
  }

  private void addSetter( @NotNull JDefinedClass jaxbObject, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo, @NotNull JFieldVar field ) {
    JMethod setter = jaxbObject.method( JMod.PUBLIC, Void.TYPE, NamingSupport.createSetter( fieldInfo.getSimpleName() ) );
    JVar param = setter.param( fieldType, fieldInfo.getSimpleName() );
    setter.body().assign( JExpr._this().ref( field ), param );
  }

  private void addGetter( @NotNull JDefinedClass jaxbObject, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo, @NotNull JFieldVar field ) {
    JMethod getter = jaxbObject.method( JMod.PUBLIC, fieldType, NamingSupport.createGetterName( fieldInfo.getSimpleName() ) );
    getter.body()._return( field );
  }

  @NotNull
  private JFieldVar addField( @NotNull JDefinedClass jaxbObject, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo ) {
    return jaxbObject.field( JMod.PRIVATE, fieldType, fieldInfo.getSimpleName() );
  }
}
