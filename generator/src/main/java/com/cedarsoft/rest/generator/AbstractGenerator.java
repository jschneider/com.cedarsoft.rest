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
import com.cedarsoft.codegen.DecisionCallback;
import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.FieldTypeInformation;
import com.sun.codemodel.JClass;
import com.sun.istack.NotNull;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.WildcardType;
import javax.annotation.Nonnull;

/**
 * @param <T> the type of the decision callback
 */
public class AbstractGenerator<T extends DecisionCallback> {
  @Nonnull
  public static final String JAXB_SUB_PACKAGE = "jaxb";

  @Nonnull
  public static final String JAXB_SUFFIX = "Jaxb";

  @Nonnull
  public static final String JAXB_STUB_SUFFIX = "Stub";
  @Nonnull
  protected final CodeGenerator codeGenerator;
  @Nonnull
  protected final DomainObjectDescriptor descriptor;
  @Nonnull
  public static final String MAPPING_SUFFIX = "Mapping";

  public AbstractGenerator( @Nonnull CodeGenerator codeGenerator, @Nonnull DomainObjectDescriptor descriptor ) {
    this.codeGenerator = codeGenerator;
    this.descriptor = descriptor;
  }

  @Nonnull
  protected String getJaxbBaseName() {
    String fqn = descriptor.getQualifiedName();
    return insertSubPackage( fqn, JAXB_SUB_PACKAGE );
  }

  @Nonnull
  protected JClass getJaxbModelType( @Nonnull TypeMirror type ) {
    return getJaxbModelType( type, false );
  }

  @Nonnull
  protected JClass getJaxbModelType( @Nonnull TypeMirror type, boolean stub ) {
    if ( TypeUtils.isSimpleType( type ) ) {
      return codeGenerator.ref( type );
    }

    if ( !isProbablyOwnType( type ) ) {
      return codeGenerator.ref( type );
    }

    if ( TypeUtils.isCollectionType( type ) ) {
      TypeMirror collectionParam = TypeUtils.getCollectionParam( type );
      JClass collection = codeGenerator.ref( TypeUtils.getErasure( type ) );

      if ( collectionParam instanceof WildcardType ) {
        return collection.narrow( codeGenerator.ref( getJaxbTypeName( TypeUtils.getErasure( collectionParam ), stub ) ).wildcard() );
      } else {
        return collection.narrow( codeGenerator.ref( getJaxbTypeName( collectionParam, stub ) ) );
      }
    }

    return codeGenerator.ref( getJaxbTypeName( type, stub ) );
  }

  @Nonnull
  protected JClass getJaxbType( @Nonnull FieldTypeInformation fieldInfo, boolean isStub ) {
    if ( TypeUtils.isCollectionType( fieldInfo.getType() ) ) {
      return codeGenerator.ref( getJaxbTypeName( TypeUtils.getErasure( TypeUtils.getCollectionParam( fieldInfo.getType() ) ), isStub ) );
    } else {
      return codeGenerator.ref( getJaxbTypeName( TypeUtils.getErasure( fieldInfo.getType() ), isStub ) );
    }
  }

  @Nonnull
  public DomainObjectDescriptor getDescriptor() {
    return descriptor;
  }

  public boolean isProbablyOwnType( @Nonnull TypeMirror type ) {
    String packageName = descriptor.getClassDeclaration().getPackage().getQualifiedName();
    if ( type.toString().startsWith( packageName ) ) {
      return true;
    }

    if ( TypeUtils.isCollectionType( type ) ) {
      return isProbablyOwnType( TypeUtils.getErasure( TypeUtils.getCollectionParam( type ) ) );
    }
    return false;
  }

  @Nonnull

  public static String insertSubPackage( @Nonnull  String fqn, @Nonnull  String packagePart ) {
    int lastIndex = fqn.lastIndexOf( '.' );
    return fqn.substring( 0, lastIndex ) + "." + packagePart + fqn.substring( lastIndex );
  }

  @Nonnull

  public static String getJaxbTypeName( @Nonnull TypeMirror type ) {
    return getJaxbTypeName( type, false );
  }

  @Nonnull

  public static String getJaxbTypeName( @Nonnull TypeMirror type, boolean stub ) {
    String base = insertSubPackage( type.toString(), JAXB_SUB_PACKAGE );
    if ( stub ) {
      return base + "$" + JAXB_STUB_SUFFIX;
    } else {
      return base + "$" + JAXB_SUFFIX;
    }
  }

  @Nonnull

  protected String getJaxbMappingTypeName() {
    String fqn = descriptor.getQualifiedName();
    return getJaxbMappingTypeName( fqn );
  }

  protected String getJaxbMappingTypeName( @Nonnull  String modelClassName ) {
    return insertSubPackage( modelClassName, JAXB_SUB_PACKAGE ) + MAPPING_SUFFIX;
  }
}
