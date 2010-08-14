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

package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.jaxb.AbstractJaxbObject;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement( name = "barModel", namespace = "http://www.cedarsoft.com/rest/generator/test/BarModel" )
public class BarModelJaxb extends AbstractJaxbObject {

  private int daInt;
  private String daString;
  private List<String> stringList;
  private List<? extends String> wildStringList;
  private Set<? extends String> set;

  public int getDaInt() {
    return daInt;
  }

  public void setDaInt( int daInt ) {
    this.daInt = daInt;
  }

  public String getDaString() {
    return daString;
  }

  public void setDaString( String daString ) {
    this.daString = daString;
  }

  public List<String> getStringList() {
    return stringList;
  }

  public void setStringList( List<String> stringList ) {
    this.stringList = stringList;
  }

  public List<? extends String> getWildStringList() {
    return wildStringList;
  }

  public void setWildStringList( List<? extends String> wildStringList ) {
    this.wildStringList = wildStringList;
  }

  public Set<? extends String> getSet() {
    return set;
  }

  public void setSet( Set<? extends String> set ) {
    this.set = set;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof BarModelJaxb ) ) return false;

    BarModelJaxb that = ( BarModelJaxb ) o;

    if ( daInt != that.daInt ) return false;
    if ( daString != null ? !daString.equals( that.daString ) : that.daString != null ) return false;
    if ( set != null ? !set.equals( that.set ) : that.set != null ) return false;
    if ( stringList != null ? !stringList.equals( that.stringList ) : that.stringList != null ) return false;
    if ( wildStringList != null ? !wildStringList.equals( that.wildStringList ) : that.wildStringList != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = daInt;
    result = 31 * result + ( daString != null ? daString.hashCode() : 0 );
    result = 31 * result + ( stringList != null ? stringList.hashCode() : 0 );
    result = 31 * result + ( wildStringList != null ? wildStringList.hashCode() : 0 );
    result = 31 * result + ( set != null ? set.hashCode() : 0 );
    return result;
  }
}
