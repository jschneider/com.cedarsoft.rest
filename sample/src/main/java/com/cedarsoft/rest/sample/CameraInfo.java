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

package com.cedarsoft.rest.sample;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class CameraInfo {
  @NotNull
  public static final CameraInfo UNKNOWN_INFO = new CameraInfo( 0, "UNKNOWN", "UNKNOWN", "UNKWNON" );

  private final long serial;
  @NotNull
  @NonNls
  private final String model;
  @NotNull
  @NonNls
  private final String make;
  @NotNull
  @NonNls
  private final String internalSerial;

  /**
   * Creates a new camera info
   *
   * @param serial         the serial
   * @param make           the make
   * @param model          the model
   * @param internalSerial the internal serial
   */
  public CameraInfo( long serial, @NotNull @NonNls String make, @NotNull @NonNls String model, @NotNull @NonNls String internalSerial ) {
    this.serial = serial;
    this.make = make;
    this.model = model;
    this.internalSerial = internalSerial;
  }

  @NotNull
  @NonNls
  public String getInternalSerial() {
    return internalSerial;
  }

  public long getSerial() {
    return serial;
  }

  @NotNull
  @NonNls
  public String getMake() {
    return make;
  }

  @NotNull
  public String getModel() {
    return model;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof CameraInfo ) ) return false;

    CameraInfo that = ( CameraInfo ) o;

    if ( serial != that.serial ) return false;
    if ( !internalSerial.equals( that.internalSerial ) ) return false;
    if ( !make.equals( that.make ) ) return false;
    if ( !model.equals( that.model ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = ( int ) ( serial ^ ( serial >>> 32 ) );
    result = 31 * result + model.hashCode();
    result = 31 * result + make.hashCode();
    result = 31 * result + internalSerial.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "CameraInfo{" +
      "model='" + model + '\'' +
      ", serial=" + serial +
      '}';
  }
}
