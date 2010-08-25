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
import com.cedarsoft.rest.AbstractJaxbTest;
import com.cedarsoft.rest.Entry;
import com.cedarsoft.rest.JaxbTestUtils;
import com.cedarsoft.rest.SimpleJaxbTest;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;
import com.sun.codemodel.fmt.JTextFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 */
public class TestGenerator extends AbstractGenerator<JaxbObjectGenerator.StubDecisionCallback> {
  @NonNls
  public static final String METHOD_NAME_SET_HREF = "setHref";
  @NonNls
  public static final String METHOD_NAME_CREATE_TEST_URI_BUILDER = "createTestUriBuilder";
  @NonNls
  public static final String METHOD_NAME_BUILD = "build";
  @NonNls
  public static final String METHOD_NAME_CREATE = "create";
  @NonNls
  public static final String METHOD_NAME_GET_RESOURCE = "getResource";
  @NonNls
  public static final String DOT_XML = ".xml";
  @NonNls
  public static final String OBJECT = "object";
  @NonNls
  public static final String TEST_SUFFIX = "JaxbTest";


  private JClass jaxbObject;
  private JClass jaxbStub;
  private JDefinedClass testClass;
  @NonNls
  public static final String DEFAULT_DATA_POINT_NAME = "default";

  public TestGenerator( @NotNull CodeGenerator<JaxbObjectGenerator.StubDecisionCallback> codeGenerator, @NotNull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  public void generateTest() throws JClassAlreadyExistsException {
    if ( jaxbObject != null || jaxbStub != null || testClass != null ) {
      throw new IllegalStateException( "Invalid state - still have generated" );
    }

    jaxbObject = codeGenerator.ref( getJaxbBaseName() + "$Jaxb" );
    jaxbStub = codeGenerator.ref( getJaxbBaseName() + "$Stub" );

    testClass = codeGenerator.getModel()._class( getTestClassName() )._extends( codeGenerator.ref( SimpleJaxbTest.class ).narrow( jaxbObject ) );

    createConstructor();

    createDataPoint( DEFAULT_DATA_POINT_NAME, jaxbObject );
    createDataPoint( "stub", jaxbStub );
  }

  private void createConstructor() {
    JMethod constructor = testClass.constructor( JMod.PUBLIC );
    constructor.body().invoke( "super" ).arg( jaxbObject.dotclass() ).arg( jaxbStub.dotclass() );
  }

  private void createDataPoint( @NotNull @NonNls String name, @NotNull JClass objectType ) {
    JMethod method = testClass.method( JMod.STATIC | JMod.PUBLIC, codeGenerator.ref( Entry.class ).narrow( objectType.wildcard() ), name );
    method.annotate( codeGenerator.ref( "org.junit.experimental.theories.DataPoint" ) );

    JVar jaxbObjectInstance = addJaxbObjectCreation( method.body(), objectType );
    method.body()._return( codeGenerator.ref( AbstractJaxbTest.class ).staticInvoke( METHOD_NAME_CREATE ).arg( jaxbObjectInstance ).arg( createGetResourceStatement( testClass ) ) );

    createTestResource( name );
  }

  @NotNull
  private JVar addJaxbObjectCreation( @NotNull JBlock block, @NotNull JClass objectType ) {
    JVar field = block.decl( objectType, OBJECT, JExpr._new( objectType ) );

    //Sets the href
    block.add( field.invoke( METHOD_NAME_SET_HREF ).arg( codeGenerator.ref( JaxbTestUtils.class ).staticInvoke( METHOD_NAME_CREATE_TEST_URI_BUILDER ).invoke( METHOD_NAME_BUILD ) ) );

    //Sets the values
    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldInfos() ) {
      JClass fieldType = getJaxbModelType( fieldInfo.getType() );

      if ( shallSkip( fieldInfo, objectType ) ) {
        continue;
      }

      JExpression value;
      if ( isProbablyOwnType( fieldInfo.getType() ) ) {
        JClass jaxbType = getJaxbType( fieldInfo, true );

        if ( TypeUtils.isCollectionType( fieldInfo.getType() ) ) {
          value = codeGenerator.getNewInstanceFactory().createCollectionInvocation( jaxbType, fieldInfo.getSimpleName(), TypeUtils.isSetType( fieldInfo.getType() ) );
        } else {
          value = codeGenerator.getNewInstanceFactory().create( jaxbType, fieldInfo.getSimpleName() );
        }
      } else {
        value = codeGenerator.getNewInstanceFactory().create( fieldType, fieldInfo.getSimpleName() );
      }

      //Now add it
      block.add( field.invoke( NamingSupport.createSetter( fieldInfo.getSimpleName() ) ).arg( value ) );
    }
    return field;
  }

  private boolean shallSkip( @NotNull FieldTypeInformation fieldInfo, @NotNull JClass objectType ) {
    if ( objectType == jaxbStub ) {
      return fieldInfo.isCollectionType();
    }

    return false;
  }

  public void createTestResource( @NotNull @NonNls String identifier ) {
    String domainObjectName = descriptor.getClassDeclaration().getSimpleName();

    String resourceName = testClass.name() + "." + identifier + DOT_XML;

    JPackage testClassPackage = testClass._package();
    if ( !testClassPackage.hasResourceFile( resourceName ) ) {
      JTextFile resource = new JTextFile( resourceName );
      resource.setContents( createSampleContent( domainObjectName ) );
      testClassPackage.addResourceFile( resource );
    }
  }

  private String createSampleContent( @NotNull String domainObjectName ) {
    String simpleName = NamingSupport.createVarName( domainObjectName );

    return "<?xml version=\"1.0\"?>\n" +
      "<" + simpleName + ">\n" +
      "</" + simpleName + ">\n"
      ;
  }

  private String getTestClassName() {
    return getJaxbBaseName() + TEST_SUFFIX;
  }

  @NotNull
  private static JExpression createGetResourceStatement( @NotNull JClass testClass ) {
    return testClass.dotclass().invoke( METHOD_NAME_GET_RESOURCE ).arg( testClass.name() + DOT_XML );
  }
}
