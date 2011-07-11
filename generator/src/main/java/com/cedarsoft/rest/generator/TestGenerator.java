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
import com.cedarsoft.codegen.NewInstanceFactory;
import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.FieldTypeInformation;
import com.cedarsoft.codegen.model.FieldWithInitializationInfo;
import com.cedarsoft.rest.test.AbstractJaxbTest;
import com.cedarsoft.rest.test.AbstractMappedJaxbTest;
import com.cedarsoft.rest.test.Entry;
import com.cedarsoft.rest.server.JaxbMapping;
import com.cedarsoft.rest.test.JaxbTestUtils;
import com.cedarsoft.rest.test.SimpleJaxbTest;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;
import com.sun.codemodel.fmt.JTextFile;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;

import javax.annotation.Nonnull;

import java.util.Arrays;

/**
 */
public class TestGenerator extends AbstractGenerator<JaxbObjectGenerator.StubDecisionCallback> {

  public static final String METHOD_NAME_SET_HREF = "setHref";

  public static final String METHOD_NAME_CREATE_TEST_URI_BUILDER = "createTestUriBuilder";

  public static final String METHOD_NAME_BUILD = "build";

  public static final String METHOD_NAME_CREATE = "create";

  public static final String METHOD_NAME_GET_RESOURCE = "getResource";

  public static final String DOT_XML = ".xml";

  public static final String OBJECT = "object";

  public static final String JAXB_TEST_SUFFIX = "JaxbTest";

  public static final String DEFAULT_DATA_POINT_NAME = "dataPoint1";

  public static final String SUPER = "super";

  public static final String MAPPING_TEST_SUFFIX = "MappingTest";

  public static final String STUB_DATA_POINT_NAME = "stub";

  public static final String COLLECTION_DATA_POINT_NAME = "collection";


  private JClass jaxbObject;
  private JClass modelType;
  private JClass jaxbStub;
  private JClass jaxbCollection;
  private JDefinedClass jaxbTestClass;
  private JDefinedClass mappingTestClass;
  private JClass mappingType;

  public static final String ID = "id";

  public TestGenerator( @Nonnull CodeGenerator codeGenerator, @Nonnull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  public void generateTest() throws JClassAlreadyExistsException {
    if ( jaxbObject != null || jaxbStub != null || jaxbTestClass != null ) {
      throw new IllegalStateException( "Invalid state - still have generated" );
    }

    modelType = codeGenerator.ref( descriptor.getQualifiedName() );
    mappingType = codeGenerator.ref( getJaxbMappingTypeName() );

    jaxbObject = codeGenerator.ref( getJaxbBaseName() + "$Jaxb" );
    jaxbStub = codeGenerator.ref( getJaxbBaseName() + "$Stub" );
    jaxbCollection = codeGenerator.ref( getJaxbBaseName() + "$Collection" );

    createJaxbTest();
    createMappingTest();
  }

  private void createMappingTest() throws JClassAlreadyExistsException {
    mappingTestClass = codeGenerator.getModel()._class( getMappingTestClassName() )
      ._extends( codeGenerator.ref( AbstractMappedJaxbTest.class )
        .narrow( modelType, jaxbObject, jaxbStub ) );

    createConstructor( mappingTestClass );

    createCreateMapping();

    createMappingDataPoint();
  }

  private void createMappingDataPoint() {
    JMethod method = mappingTestClass.method( JMod.STATIC | JMod.PUBLIC, codeGenerator.ref( Entry.class ).narrow( modelType.wildcard() ), DEFAULT_DATA_POINT_NAME );
    method.annotate( codeGenerator.ref( "org.junit.experimental.theories.DataPoint" ) );

    JVar field = method.body().decl( modelType, OBJECT, createDomainObjectCreationExpression() );

    method.body()._return( codeGenerator.ref( AbstractJaxbTest.class ).staticInvoke( METHOD_NAME_CREATE )
      .arg( field )
      .arg( createGetResourceStatement( mappingTestClass, DEFAULT_DATA_POINT_NAME ) )
      .arg( createGetResourceStatement( mappingTestClass, STUB_DATA_POINT_NAME ) )
    );

    createTestResource( mappingTestClass, DEFAULT_DATA_POINT_NAME );
    createTestResource( mappingTestClass, STUB_DATA_POINT_NAME );
  }

  @Nonnull
  protected JInvocation createDomainObjectCreationExpression() {
    JInvocation invocation = JExpr._new( codeGenerator.ref( descriptor.getQualifiedName() ) );

    ConstructorDeclaration constructor = descriptor.findBestConstructor();
    for ( ParameterDeclaration parameterDeclaration : constructor.getParameters() ) {
      invocation.arg( codeGenerator.getNewInstanceFactory().create( parameterDeclaration.getType(), parameterDeclaration.getSimpleName() ) );
    }

    return invocation;
  }

  private void createCreateMapping() {
    JMethod method = mappingTestClass.method( JMod.PROTECTED, codeGenerator.ref( JaxbMapping.class ).narrow( modelType, jaxbObject, jaxbStub ), "createMapping" );
    method.annotate( Override.class );
    method.body()._return( JExpr._new( mappingType ) );
  }

  private void createJaxbTest() throws JClassAlreadyExistsException {
    jaxbTestClass = codeGenerator.getModel()._class( getJaxbTestClassName() )._extends( codeGenerator.ref( SimpleJaxbTest.class )
      .narrow( jaxbObject )
      .narrow( jaxbStub )
    );

    createConstructor( jaxbTestClass );

    createDataPoint( DEFAULT_DATA_POINT_NAME, jaxbObject );
    createDataPoint( STUB_DATA_POINT_NAME, jaxbStub );
    createCollectionDataPoint( COLLECTION_DATA_POINT_NAME );
  }

  private void createCollectionDataPoint( String identifier ) {
    JMethod method = createDataPointMethod( identifier, jaxbCollection );

    JVar stub0 = method.body().decl( jaxbStub, "firstStub", JExpr._new( jaxbStub ).arg( "daId0" ) );

    JBlock block0 = new JBlock( true, true );
    method.body().add( block0 );
    addFieldCopy( block0, jaxbStub, stub0 );


    JVar stub1 = method.body().decl( jaxbStub, "secondStub", JExpr._new( jaxbStub ).arg( "daId1" ) );

    JBlock block1 = new JBlock( true, true );
    method.body().add( block1 );
    addFieldCopy( block1, jaxbStub, stub1 );

    JExpression stubsExpression = codeGenerator.getClassRefSupport().ref( Arrays.class ).staticInvoke( NewInstanceFactory.METHOD_NAME_AS_LIST )
      .arg( stub0 )
      .arg( stub1 );
    JVar jaxbObjectInstance = method.body().decl( jaxbCollection, OBJECT, JExpr._new( jaxbCollection ).arg( stubsExpression ) );

    //Sets the href
    addHrefSet( method.body(), jaxbObjectInstance );


    method.body()._return( JExpr.invoke( METHOD_NAME_CREATE ).arg( jaxbObjectInstance ).arg( createGetResourceStatement( jaxbTestClass, identifier ) ) );

    createTestResource( jaxbTestClass, identifier );
  }

  private void createConstructor( @Nonnull JDefinedClass parent ) {
    JMethod constructor = parent.constructor( JMod.PUBLIC );
    JInvocation superInvocation = constructor.body().invoke( SUPER ).arg( jaxbObject.dotclass() ).arg( jaxbStub.dotclass() );

    if ( parent == jaxbTestClass ) {
      superInvocation.arg( jaxbCollection.dotclass() );
    }
  }

  private void createDataPoint( @Nonnull  String identifier, @Nonnull JClass objectType ) {
    JMethod method = createDataPointMethod( identifier, objectType );

    JVar jaxbObjectInstance = addJaxbObjectCreation( method.body(), objectType );

    method.body()._return( JExpr.invoke( METHOD_NAME_CREATE ).arg( jaxbObjectInstance ).arg( createGetResourceStatement( jaxbTestClass, identifier ) ) );

    createTestResource( jaxbTestClass, identifier );
  }

  private JMethod createDataPointMethod( @Nonnull  String identifier, @Nonnull JClass objectType ) {
    JMethod method = jaxbTestClass.method( JMod.STATIC | JMod.PUBLIC, codeGenerator.ref( Entry.class ).narrow( objectType.wildcard() ), identifier );
    method.annotate( codeGenerator.ref( "org.junit.experimental.theories.DataPoint" ) );
    return method;
  }

  @Nonnull
  private JVar addJaxbObjectCreation( @Nonnull JBlock block, @Nonnull JClass objectType ) {
    JVar field = block.decl( objectType, OBJECT, JExpr._new( objectType ).arg( "daId" ) );
    addFieldCopy( block, objectType, field );
    return field;
  }

  private void addFieldCopy( @Nonnull JBlock block, @Nonnull JClass objectType, @Nonnull JVar targetObject ) {
    //Sets the href
    addHrefSet( block, targetObject );

    //Sets the values
    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldInfos() ) {
      JClass fieldType = getJaxbModelType( fieldInfo.getType() );

      if ( fieldInfo.getSimpleName().equals( ID ) ) {
        continue;
      }

      if ( shallSkip( fieldInfo, objectType ) ) {
        continue;
      }

      JExpression value;
      if ( isProbablyOwnType( fieldInfo.getType() ) ) {
        JClass fieldStubType = getJaxbType( fieldInfo, true );
        JClass fieldJaxbType = getJaxbType( fieldInfo, false );

        if ( TypeUtils.isCollectionType( fieldInfo.getType() ) ) {
          value = codeGenerator.getNewInstanceFactory().createCollectionInvocation( fieldStubType, fieldInfo.getSimpleName(), TypeUtils.isSetType( fieldInfo.getType() ) );
        } else {
          JClass fieldTypeToInstantiate = objectType == this.jaxbStub ? fieldStubType : fieldJaxbType;
          value = codeGenerator.getNewInstanceFactory().create( fieldTypeToInstantiate, fieldInfo.getSimpleName() );
        }
      } else {
        value = codeGenerator.getNewInstanceFactory().create( fieldType, fieldInfo.getSimpleName() );
      }

      //Now add it
      block.add( targetObject.invoke( NamingSupport.createSetter( fieldInfo.getSimpleName() ) ).arg( value ) );
    }
  }

  private void addHrefSet( JBlock block, JVar field ) {
    block.add( field.invoke( METHOD_NAME_SET_HREF ).arg( codeGenerator.ref( JaxbTestUtils.class ).staticInvoke( METHOD_NAME_CREATE_TEST_URI_BUILDER ).invoke( METHOD_NAME_BUILD ) ) );
  }

  private boolean shallSkip( @Nonnull FieldTypeInformation fieldInfo, @Nonnull JClass objectType ) {
    if ( objectType == jaxbStub ) {
      return fieldInfo.isCollectionType();
    }

    if ( objectType == jaxbCollection ) {
      return true;
    }

    return false;
  }

  public void createTestResource( @Nonnull JClass testClass, @Nonnull  String identifier ) {
    String resourceName = createResourceName( testClass.name(), identifier );

    JPackage testClassPackage = testClass._package();
    if ( !testClassPackage.hasResourceFile( resourceName ) ) {
      JTextFile resource = new JTextFile( resourceName );
      resource.setContents( createSampleContent() );
      testClassPackage.addResourceFile( resource );
    }
  }

  private String getJaxbTestClassName() {
    return getJaxbBaseName() + JAXB_TEST_SUFFIX;
  }

  private String getMappingTestClassName() {
    return getJaxbBaseName() + MAPPING_TEST_SUFFIX;
  }

  private static String createResourceName( @Nonnull  String testName, @Nonnull  String identifier ) {
    return testName + "." + identifier + DOT_XML;
  }

  @Nonnull
  private static JExpression createGetResourceStatement( @Nonnull JClass testClass, @Nonnull  String identifier ) {
    return testClass.dotclass().invoke( METHOD_NAME_GET_RESOURCE ).arg( createResourceName( testClass.name(), identifier ) );
  }

  @Nonnull

  private static String createSampleContent() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
  }
}
