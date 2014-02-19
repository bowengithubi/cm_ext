// Copyright (c) 2013 Cloudera, Inc. All rights reserved.
package com.cloudera.csd;

import static org.junit.Assert.*;

import com.cloudera.csd.StringInterpolator.VariableProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class StringInterpolatorTest {
  private Map<String, String> objects = ImmutableMap.of("var1", "value1",
          "var2", "value2");
  private VariableProvider provider = new MapVariableProvider();

  private StringInterpolator interpolator = new StringInterpolator();

  @Test
  public void testInterpolate() {
    assertEquals("My value1 is value2", format("My ${var1} is ${var2}"));
  }

  @Test
  public void testInterpolateMultiple() {
    assertEquals("My value1 is value2 value1",
        format("My ${var1} is ${var2} ${var1}"));
  }

  @Test
  public void testInterpolateNoVars() {
    assertEquals("Stuff is rad", format("Stuff is rad"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInterpolateBadVars() {
    format("My ${badArg}, ${var1}");
  }

  @Test
  public void testInterpolateValues() {
    Map<String, String> map = ImmutableMap.of(
            "key1", "This is ${var1}",
            "key2", "This is ${var2}");

    Map<String, String> convertedMap = ImmutableMap.of(
            "key1", "This is value1",
            "key2", "This is value2");
    assertEquals(convertedMap,
            interpolator.interpolateValues(map, provider));
  }

  @Test
  public void testInterpolateValuesNull() {
    assertEquals(ImmutableMap.<String, String>of(),
            interpolator.interpolateValues(null, provider));
  }

  @Test
  public void testInterpolateList() {
    List<String> list = ImmutableList.of("This is ${var1}",
                                         "This is ${var2}");
    List<String> convertedList = ImmutableList.of("This is value1",
                                                  "This is value2");
    assertEquals(convertedList,
            interpolator.interpolateList(list, provider));
  }

  @Test
  public void testInterpolateListNull() {
    assertEquals(ImmutableList.<String>of(),
                 interpolator.interpolateList(null, provider));
  }

  private String format(String template) {
    return interpolator.interpolate(template, objects);
  }

  public class MapVariableProvider implements VariableProvider {
    public String provide(String variableName) {
      return objects.get(variableName);
    }
  }
}