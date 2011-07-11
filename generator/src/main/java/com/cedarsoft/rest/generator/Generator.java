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
import com.cedarsoft.codegen.JAnnotationFieldReference;
import com.cedarsoft.codegen.NamingSupport;
import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.FieldInfo;
import com.cedarsoft.codegen.model.FieldTypeInformation;
import com.cedarsoft.codegen.model.FieldWithInitializationInfo;
import com.cedarsoft.id.NameSpaceSupport;
import com.cedarsoft.rest.model.AbstractJaxbCollection;
import com.cedarsoft.rest.model.AbstractJaxbObject;
import com.cedarsoft.rest.model.JaxbObject;
import com.cedarsoft.rest.model.JaxbStub;
import com.cedarsoft.rest.server.JaxbMapping;
import com.cedarsoft.rest.server.UriContext;
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
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import javax.annotation.Nonnull;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class Generator extends AbstractGenerator<JaxbObjectGenerator.StubDecisionCallback> {

  public static final String ID = "id";
  @Nonnull

  public static final String METHOD_NAME_GET_STUB = "getStub";
  @Nonnull

  public static final String METHOD_NAME_GET = "get";

  public static final String METHOD_NAME_SET_ID = "setId";

  public static final String METHOD_NAME_GET_ID = "getId";

  public static final String METHOD_NAME_GET_URIS = "getUri";

  public static final String METHOD_NAME_PATH = "path";

  public static final String METHOD_NAME_BUILD = "build";

  public static final String ARG_PLACEHOLDER_ID = "{id}";

  public static final String METHOD_NAME_SET_HREF = "setHref";

  public static final String NAME = "name";

  public static final String NAMESPACE = "namespace";

  public static final String VALUE = "value";

  public static final String OBJECT = "object";

  public static final String CONTEXT = "context";

  public static final String JAXB_OBJECT = "jaxbObject";

  public static final String STUB_OBJECT = "stub";

  public static final String METHOD_NAME_CREATE_JAXB_OBJECT = "createJaxbObject";

  public static final String METHOD_NAME_CREATE_JAXB_STUB = "createJaxbStub";

  public static final String STUB_NAMESPACE_SUFFIX = "/stub";

  public static final String METHOD_NAME_ADD_MAPPING = "addMapping";

  public static final String METHOD_NAME_GET_DELEGATES_MAPPING = "getDelegatesMapping";

  public static final String JAXB = "Jaxb";

  public static final String STUB = "Stub";

  public static final String COLLECTION = "Collection";

  public static final String CONST_PATH = "PATH";

  public static final String CONST_ID = "ID";

  public static final String METHOD_GET_BASE_URI_BUILDER = "getBaseUriBuilder";

  private JFieldVar ns;
  private JFieldVar nsStub;
  private JFieldVar nsCollection;
  @Nonnull

  private final String varName = NamingSupport.createVarName( descriptor.getClassDeclaration().getSimpleName() );
  private final String pluralName = NamingSupport.plural( NamingSupport.createVarName( descriptor.getClassDeclaration().getSimpleName() ) );

  public Generator( @Nonnull CodeGenerator codeGenerator, @Nonnull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  private JDefinedClass baseClass;
  private JDefinedClass jaxbObject;
  private JDefinedClass jaxbStub;
  private JDefinedClass jaxbCollection;
  private JDefinedClass mappingClass;

  public void generate() throws JClassAlreadyExistsException {
    if ( baseClass != null || jaxbObject != null || jaxbStub != null || mappingClass != null ) {
      throw new IllegalStateException( "generate has still been called!" );
    }

    createBaseClass();

    createJaxbObject();
    createJaxbStub();
    createJaxbCollection();

    createJaxbMapping();
  }

  private void createBaseClass() throws JClassAlreadyExistsException {
    baseClass = _class( getJaxbBaseName(), JMod.PUBLIC | JMod.ABSTRACT )._extends( AbstractJaxbObject.class );
    baseClass.annotate( XmlTransient.class );
    baseClass.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );

    addNameSpaces();

    addFields( baseClass, Scope.COMMON );
    addConstructors( baseClass, JMod.PROTECTED );
  }

  private void addNameSpaces() {
    ns = baseClass.field( JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class, "NS", JExpr.lit( NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) ) );
    nsStub = baseClass.field( JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class, "NS_STUB", ns.plus( JExpr.ref( "NS_STUB_SUFFIX" ) ) );
    nsCollection = baseClass.field( JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class, "NS_COLLECTION", nsStub.plus( JExpr.ref( "NS_COLLECTION_SUFFIX" ) ) );
  }

  private void addConstructors( @Nonnull JDefinedClass type, int mod ) {
    //Default constructor
    type.constructor( mod );

    //constructor with id
    JMethod constructor = type.constructor( mod );
    JVar id = constructor.param( String.class, ID );

    constructor.body().invoke( "super" ).arg( id );
  }

  /**
   * @noinspection InstanceMethodNamingConvention
   */
  @Nonnull
  protected JDefinedClass _class( @Nonnull  String jaxbBaseName, int mods ) throws JClassAlreadyExistsException {
    int idx = jaxbBaseName.lastIndexOf( '.' );
    return codeGenerator.getModel()._package( jaxbBaseName.substring( 0, idx ) )._class( mods, jaxbBaseName.substring( idx + 1 ) );
  }

  private void createJaxbMapping() throws JClassAlreadyExistsException {
    assert jaxbObject != null;
    assert jaxbStub != null;

    JClass objectType = codeGenerator.ref( descriptor.getQualifiedName() );
    JClass superType = codeGenerator.ref( JaxbMapping.class ).narrow( objectType ).narrow( jaxbObject ).narrow( jaxbStub );

    mappingClass = codeGenerator.getModel()._class( getJaxbMappingTypeName() )._extends( superType );

    createHrefMethod();
    createCreateJaxbMethod( jaxbObject, METHOD_NAME_CREATE_JAXB_OBJECT );
    createCreateJaxbMethod( jaxbStub, METHOD_NAME_CREATE_JAXB_STUB );

    createCopyMethod( "copyFieldsToJaxbObject", jaxbObject, false );
    createCopyMethod( "copyFieldsToStub", jaxbStub, true );
  }

  private void createCreateJaxbMethod( @Nonnull JClass jaxbType, @Nonnull  String methodName ) {
    JMethod method = mappingClass.method( JMod.PROTECTED, jaxbType, methodName );
    method.annotate( Override.class );

    JVar object = method.param( codeGenerator.ref( descriptor.getQualifiedName() ), OBJECT );
    method.body()._return( JExpr._new( jaxbType ).arg( object.invoke( METHOD_NAME_GET_ID ) ) );
  }

  private void createCopyMethod( String methodName, JDefinedClass targetType, boolean stub ) {
    JMethod method = mappingClass.method( JMod.PROTECTED, Void.TYPE, methodName );
    method.annotate( Override.class );

    JVar source = method.param( codeGenerator.ref( descriptor.getQualifiedName() ), "source" );
    JVar target = method.param( targetType, "target" );
    JVar context = method.param( codeGenerator.ref( UriContext.class ), CONTEXT );

    addFieldCopyOperations( source, target, context, method.body(), stub );
  }

  private void addFieldCopyOperations( @Nonnull JExpression source, @Nonnull JExpression target, @Nonnull JExpression context, @Nonnull JBlock block, boolean isStub ) {
    assert jaxbObject != null;
    assert jaxbStub != null;
    assert mappingClass != null;


    Collection<JStatement> statements = new ArrayList<JStatement>();

    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldInfos() ) {
      JInvocation getterInvocation = source.invoke( fieldInfo.getGetterDeclaration().getSimpleName() );

      if ( fieldInfo.getSimpleName().equals( ID ) ) {
        continue;
      }


      if ( isStub && !shallAddFieldCopyStatementToStub( fieldInfo ) ) {
        continue;
      }

      JInvocation value;
      if ( isProbablyOwnType( fieldInfo.getType() ) ) {
        JClass fieldJaxbType = getJaxbType( fieldInfo, false );
        JClass fieldStubType = getJaxbType( fieldInfo, true );

        if ( isStub || TypeUtils.isCollectionType( fieldInfo.getType() ) ) {
          value = JExpr.invoke( METHOD_NAME_GET_STUB )
            .arg( fieldStubType.dotclass() )
            .arg( getterInvocation )
            .arg( context )
            ;
        } else {
          value = JExpr.invoke( METHOD_NAME_GET )
            .arg( fieldJaxbType.dotclass() )
            .arg( getterInvocation )
            .arg( context )
            ;
        }

        ensureDelegateAvailable( fieldJaxbType, fieldStubType );
      } else {
        value = getterInvocation;
      }
      statements.add( target.invoke( NamingSupport.createSetter( fieldInfo.getSimpleName() ) ).arg( value ) );
    }

    for ( JStatement statement : statements ) {
      block.add( statement );
    }
  }

  private void ensureDelegateAvailable( @Nonnull JClass fieldJaxbObject, @Nonnull JClass fieldJaxbStub ) {
    JMethod constructor = getOrCreateConstructor();

    String paramName = NamingSupport.createVarName( fieldJaxbObject.outer().name() + MAPPING_SUFFIX );

    JClass mappingType = codeGenerator.ref( getMappingNameFor( fieldJaxbObject ) );

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
        .arg( fieldJaxbObject.dotclass() )
        .arg( fieldJaxbStub.dotclass() )
        .arg( param )
    );
  }

  @Nonnull

  private static String getMappingNameFor( @Nonnull JClass jaxbObject ) {
    return jaxbObject.outer().fullName() + MAPPING_SUFFIX;
  }

  @Nonnull
  private JMethod getOrCreateConstructor() {
    assert mappingClass != null;

    //If no constructor exists, create one
    if ( !mappingClass.constructors().hasNext() ) {
      mappingClass.constructor( JMod.PUBLIC );
    }

    return ( JMethod ) mappingClass.constructors().next();
  }

  private void createHrefMethod() {
    assert mappingClass != null;

    JMethod method = mappingClass.method( JMod.PROTECTED, UriBuilder.class, METHOD_NAME_GET_URIS );
    JVar object = method.param( JaxbObject.class, OBJECT );
    JVar context = method.param( UriContext.class, CONTEXT );
    method.annotate( Override.class );

    JFieldVar pathConst = mappingClass.field( JMod.PUBLIC | JMod.STATIC | JMod.FINAL, String.class, CONST_PATH,
                                              JExpr.lit( NamingSupport.plural( NamingSupport.createXmlElementName( getDescriptor().getClassDeclaration().getSimpleName() ) ) ) );
    method.body()._return( context.invoke( METHOD_GET_BASE_URI_BUILDER ).invoke( METHOD_NAME_PATH ).arg( pathConst ).invoke( METHOD_NAME_PATH ).arg( object.invoke( METHOD_NAME_GET_ID ) ) );
  }

  protected void createJaxbObject() throws JClassAlreadyExistsException {
    assert baseClass != null;

    jaxbObject = baseClass._class( JMod.PUBLIC | JMod.STATIC, JAXB )._extends( baseClass );
    JAnnotationFieldReference.param(
      jaxbObject.annotate( XmlType.class ).param( NAME, varName ),
      NAMESPACE, ns );
    JAnnotationFieldReference.param(
      jaxbObject.annotate( XmlRootElement.class ).param( NAME, varName ),
      NAMESPACE, ns
    );
    jaxbObject.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );

    addConstructors( jaxbObject, JMod.PUBLIC );
    addFields( jaxbObject, Scope.JAXB );
  }

  protected void createJaxbStub() throws JClassAlreadyExistsException {
    assert baseClass != null;
    assert jaxbObject != null;

    String nameWithStub = varName + STUB;

    jaxbStub = baseClass._class( JMod.PUBLIC | JMod.STATIC, STUB )._extends( baseClass )._implements(
      codeGenerator.ref( JaxbStub.class ).narrow( jaxbObject )
    );

    jaxbStub.narrow( jaxbObject );

    JAnnotationFieldReference.param(
      jaxbStub.annotate( XmlType.class )
        .param( NAME, nameWithStub ),
      NAMESPACE, nsStub );
    JAnnotationFieldReference.param(
      jaxbStub.annotate( XmlRootElement.class )
        .param( NAME, varName ),
      NAMESPACE, nsStub
    );
    jaxbStub.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );


    addConstructors( jaxbStub, JMod.PUBLIC );
    addFields( jaxbStub, Scope.STUB );

    addGetJaxbType();
  }

  protected void createJaxbCollection() throws JClassAlreadyExistsException {
    jaxbCollection = baseClass._class( JMod.PUBLIC | JMod.STATIC, COLLECTION )._extends( codeGenerator.ref( AbstractJaxbCollection.class ) );

    JAnnotationFieldReference.param(
      jaxbCollection.annotate( XmlType.class )
        .param( NAME, pluralName ),
      NAMESPACE, nsCollection );
    JAnnotationFieldReference.param(
      jaxbCollection.annotate( XmlRootElement.class )
        .param( NAME, pluralName )
        .param( NAMESPACE, NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) + STUB_NAMESPACE_SUFFIX ),
      NAMESPACE, nsCollection
    );
    jaxbCollection.annotate( XmlAccessorType.class ).param( VALUE, XmlAccessType.FIELD );


    JClass stubsListType = codeGenerator.ref( List.class ).narrow( jaxbStub );
    JFieldVar stubsField = jaxbCollection.field( JMod.PRIVATE, stubsListType, pluralName );
    stubsField.annotate( XmlElementRef.class );

    addGetter( jaxbCollection, stubsListType, stubsField, stubsField.name() );
    addSetter( jaxbCollection, stubsListType, stubsField, stubsField.name() );

    //Add constructors
    jaxbCollection.constructor( JMod.PUBLIC );

    {
      JMethod constructor = jaxbCollection.constructor( JMod.PUBLIC );
      JVar stubsParam = constructor.param( stubsListType, stubsField.name() );
      constructor.body().invoke( "this" ).arg( stubsParam ).arg( JExpr.lit( 0 ) ).arg( JExpr.lit( 0 ) );
    }

    {
      JMethod constructor = jaxbCollection.constructor( JMod.PUBLIC );
      JVar stubsParam = constructor.param( stubsListType, stubsField.name() );
      JVar startIndex = constructor.param( Integer.TYPE, "startIndex" );
      JVar maxLength = constructor.param( Integer.TYPE, "maxLength" );
      constructor.body().invoke( "super" ).arg( startIndex ).arg( maxLength );

      constructor.body().assign( JExpr.refthis( stubsField.name() ), stubsParam );
    }
  }


  private void addGetJaxbType() {
    assert jaxbStub != null;
    assert jaxbObject != null;

    JMethod method = jaxbStub.method( JMod.PUBLIC, codeGenerator.ref( Class.class ).narrow( jaxbObject ), "getJaxbType" );
    method.annotate( Override.class );
    method.body()._return( jaxbObject.dotclass() );
  }

  /**
   * Adds all fields
   *
   * @param currentClass the jaxb class
   * @param type         the type
   */
  private void addFields( @Nonnull JDefinedClass currentClass, @Nonnull Scope type ) {
    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldInfos() ) {
      //Skip the id, since it is defined in the super class
      if ( fieldInfo.getSimpleName().equals( ID ) ) {
        continue;
      }

      if ( !shallAddField( fieldInfo, type ) ) {
        continue;
      }

      boolean isCollectionType = TypeUtils.isCollectionType( fieldInfo.getType() );

      //If it is a collection --> always create a stub
      //And for stubs also create stubs, otherwise create a "real" object
      JClass fieldType = getJaxbModelType( fieldInfo.getType(), isCollectionType || type == Scope.STUB );
      JFieldVar field = addField( currentClass, fieldType, fieldInfo );

      if ( isCollectionType ) {
        JAnnotationUse annotation = field.annotate( XmlElement.class );
        annotation.param( "name", NamingSupport.createSingular( field.name() ) );
      }

      addGetter( currentClass, fieldType, field, fieldInfo.getSimpleName() );
      addSetter( currentClass, fieldType, field, fieldInfo.getSimpleName() );
    }
  }

  private boolean shallAddFieldCopyStatementToStub( @Nonnull FieldTypeInformation fieldInfo ) {
    return !fieldInfo.isCollectionType();
  }

  protected boolean shallAddField( @Nonnull FieldTypeInformation fieldInfo, @Nonnull Scope type ) {
    return ( ( JaxbObjectGenerator.StubDecisionCallback ) codeGenerator.getDecisionCallback() ).shallAddFieldStatement( this, fieldInfo, type );
  }

  private static void addSetter( @Nonnull JDefinedClass currentClass, @Nonnull JType fieldType, @Nonnull JVar field, String name ) {
    JMethod setter = currentClass.method( JMod.PUBLIC, Void.TYPE, NamingSupport.createSetter( name ) );
    JVar param = setter.param( fieldType, name );
    setter.body().assign( JExpr._this().ref( field ), param );
  }

  private static void addGetter( @Nonnull JDefinedClass currentClass, @Nonnull JType fieldType, @Nonnull JExpression field, @Nonnull  String name ) {
    JMethod getter = currentClass.method( JMod.PUBLIC, fieldType, NamingSupport.createGetterName( name ) );
    getter.body()._return( field );
  }

  @Nonnull
  private static JFieldVar addField( @Nonnull JDefinedClass currentClass, @Nonnull JType fieldType, @Nonnull FieldInfo fieldInfo ) {
    return currentClass.field( JMod.PRIVATE, fieldType, fieldInfo.getSimpleName() );
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
