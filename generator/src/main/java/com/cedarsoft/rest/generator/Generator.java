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
import com.cedarsoft.jaxb.JaxbObject;
import com.cedarsoft.jaxb.JaxbStub;
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
import javax.xml.bind.annotation.XmlType;
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
  @NonNls
  public static final String METHOD_NAME_ADD_MAPPING = "addMapping";
  @NonNls
  public static final String METHOD_NAME_GET_DELEGATES_MAPPING = "getDelegatesMapping";
  @NonNls
  public static final String JAXB = "Jaxb";
  @NonNls
  public static final String STUB = "Stub";
  @NonNls
  public static final String CONST_PATH = "PATH";
  @NonNls
  public static final String CONST_ID = "ID";

  public Generator( @NotNull CodeGenerator<JaxbObjectGenerator.StubDecisionCallback> codeGenerator, @NotNull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  public void generate() throws JClassAlreadyExistsException {
    JDefinedClass baseClass = createBaseClass();

    JDefinedClass jaxbObject = createJaxbObject( baseClass );
    JDefinedClass jaxbStub = createJaxbStub( baseClass, jaxbObject );

    createJaxbMapping( jaxbObject, jaxbStub );
  }

  @NotNull
  private JDefinedClass createBaseClass() throws JClassAlreadyExistsException {
    JDefinedClass baseClass = _class( getJaxbBaseName(), JMod.PUBLIC | JMod.ABSTRACT )._extends( AbstractJaxbObject.class );
    baseClass.annotate( XmlType.class )
      .param( NAME, "abstract" + descriptor.getClassDeclaration().getSimpleName() );

    addFields( baseClass, Scope.COMMON );

    return baseClass;
  }

  /**
   * @noinspection InstanceMethodNamingConvention
   */
  @NotNull
  private JDefinedClass _class( @NotNull @NonNls String jaxbBaseName, int mods ) throws JClassAlreadyExistsException {
    int idx = jaxbBaseName.lastIndexOf( '.' );
    return codeGenerator.getModel()._package( jaxbBaseName.substring( 0, idx ) )._class( mods, jaxbBaseName.substring( idx + 1 ) );
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

      if ( isStub && !shallAddFieldCopyStatementToStub( fieldInfo ) ) {
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

        ensureDelegateAvailable( mappingClass, fieldJaxbType,  );

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

  private void ensureDelegateAvailable( @NotNull JDefinedClass mappingClass, @NotNull JClass jaxbObject, @NotNull JClass jaxbStub ) {
    JMethod constructor = getOrCreateConstructor( mappingClass );

    String paramName = NamingSupport.createVarName( jaxbObject.outer().name() + MAPPING_SUFFIX );

    JClass mappingType = codeGenerator.ref( getMappingNameFor( jaxbObject ) );

    //Check whether the mapping still exists
    for ( JVar param : constructor.listParams() ) {
      if ( param.type().equals( mappingType ) ) {
        return;
      }
    }

    //It does not exist, therefore let us add the serializer and map it
    JVar param = constructor.param( mappingType, paramName );

    constructor.body().add(
      JExpr.invoke( METHOD_NAME_GET_DELEGATES_MAPPING ).invoke( METHOD_NAME_ADD_MAPPING )
        .arg( jaxbObject.dotclass() )
        .arg( jaxbStub.dotclass() )
        .arg( param )
    );
  }

  @NotNull
  @NonNls
  private static String getMappingNameFor( @NotNull JClass jaxbObject ) {
    return jaxbObject.outer().fullName() + MAPPING_SUFFIX;
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

    JFieldVar pathConst = mappingClass.field( JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class, CONST_PATH,
                                              JExpr.lit( NamingSupport.plural( NamingSupport.createXmlElementName( getDescriptor().getClassDeclaration().getSimpleName() ) ) ) );
    JFieldVar idConst = mappingClass.field( JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class, CONST_ID, JExpr.lit( ARG_PLACEHOLDER_ID ) );

    JInvocation uriBuilderInvocation = uriBuilder.invoke( METHOD_NAME_PATH )
      .arg( pathConst )
      .invoke( METHOD_NAME_PATH )
      .arg( idConst )
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
    return insertSubPackage( modelClassName, JAXB_SUB_PACKAGE ) + MAPPING_SUFFIX;
  }

  @NotNull
  protected JDefinedClass createJaxbObject( @NotNull JDefinedClass baseClass ) throws JClassAlreadyExistsException {
    String name = NamingSupport.createVarName( descriptor.getClassDeclaration().getSimpleName() );

    JDefinedClass jaxbObject = baseClass._class( JMod.PUBLIC | JMod.STATIC, JAXB )._extends( baseClass );
    jaxbObject.annotate( XmlType.class )
      .param( NAME, name );
    jaxbObject.annotate( XmlRootElement.class )
      .param( NAME, name )
      .param( NAMESPACE, NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) );
    jaxbObject.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );

    addFields( jaxbObject, Scope.JAXB );

    return jaxbObject;
  }

  @NotNull
  protected JDefinedClass createJaxbStub( @NotNull JDefinedClass baseClass, @NotNull JDefinedClass jaxbObject ) throws JClassAlreadyExistsException {
    String name = NamingSupport.createVarName( descriptor.getClassDeclaration().getSimpleName() ) + STUB;

    JDefinedClass jaxbStub = baseClass._class( JMod.PUBLIC | JMod.STATIC, STUB )._extends( baseClass )._implements(
      codeGenerator.ref( JaxbStub.class ).narrow( jaxbObject )
    );

    jaxbStub.narrow( jaxbObject );

    jaxbStub.annotate( XmlType.class )
      .param( NAME, name );
    jaxbStub.annotate( XmlRootElement.class )
      .param( NAME, NamingSupport.createVarName( descriptor.getClassDeclaration().getSimpleName() ) )
      .param( NAMESPACE, NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) + STUB_NAMESPACE_SUFFIX );
    jaxbStub.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );


    addFields( jaxbStub, Scope.STUB );

    addGetJaxbType( jaxbStub, jaxbObject );

    return jaxbStub;
  }

  private void addGetJaxbType( @NotNull JDefinedClass stub, @NotNull JDefinedClass jaxbObject ) {
    JMethod method = stub.method( JMod.PUBLIC, codeGenerator.ref( Class.class ).narrow( jaxbObject ), "getJaxbType" );
    method.annotate( Override.class );
    method.body()._return( jaxbObject.dotclass() );
  }

  /**
   * Adds all fields
   *
   * @param jaxbObject the jaxb class
   */
  private void addFields( @NotNull JDefinedClass jaxbObject, @NotNull Scope type ) {
    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldInfos() ) {
      //Skip the id, since it is defined in the super class
      if ( fieldInfo.getSimpleName().equals( ID ) ) {
        continue;
      }

      if ( !shallAddField( fieldInfo, type ) ) {
        continue;
      }

      JClass fieldType = getJaxbModelType( fieldInfo.getType(), type.isStub() );
      JFieldVar field = addField( jaxbObject, fieldType, fieldInfo );

      if ( TypeUtils.isCollectionType( fieldInfo.getType() ) ) {
        JAnnotationUse annotation = field.annotate( XmlElement.class );
        annotation.param( "name", NamingSupport.createSingular( field.name() ) );
      }

      addGetter( jaxbObject, fieldType, fieldInfo, field );
      addSetter( jaxbObject, fieldType, fieldInfo, field );
    }
  }

  private boolean shallAddFieldCopyStatementToStub( @NotNull FieldTypeInformation fieldInfo ) {
    return isProbablyOwnType( fieldInfo.getType() );
  }

  protected boolean shallAddField( @NotNull FieldTypeInformation fieldInfo, @NotNull Scope type ) {
    return codeGenerator.getDecisionCallback().shallAddFieldStatement( this, fieldInfo, type );
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

  public enum Scope {
    COMMON,
    JAXB,
    STUB;

    public boolean isStub() {
      return this == STUB;
    }
  }
}
