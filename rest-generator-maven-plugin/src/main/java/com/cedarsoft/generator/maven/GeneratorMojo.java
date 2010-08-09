package com.cedarsoft.generator.maven;

import com.cedarsoft.codegen.AbstractGenerator;
import com.cedarsoft.rest.generator.Generator;
import com.cedarsoft.rest.generator.JaxbObjectGenerator;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @goal generate
 */
public class GeneratorMojo extends AbstractGenerateMojo {
  @NotNull
  @Override
  protected AbstractGenerator createGenerator() {
    return new JaxbObjectGenerator();
  }
}
