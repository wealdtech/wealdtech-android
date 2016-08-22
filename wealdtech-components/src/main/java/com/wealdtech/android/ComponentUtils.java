/*
 * Copyright 2012 - 2016 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android;

import android.widget.TextView;

import javax.annotation.Nullable;

/**
 * General-purpose utilities for components
 */
public class ComponentUtils
{
  /**
   * Obtain a clean version of a text view's contents
   *
   * @param view the view
   *
   * @return a clean version of the contents of the text view; empty string will be {@code null} rather than ""
   */
  @Nullable
  public static String getCleanText(final TextView view)
  {
    return getCleanText(view.getText().toString());
  }

  /**
   * Obtain a clean string given an input string
   *
   * @param val the input string
   *
   * @return a clean version of the contents of the string; empty string will be {@code null} rather than ""
   */
  @Nullable
  public static String getCleanText(String val)
  {
    if (val != null)
    {
      val = val.trim();
    }
    if (val == null || val.equals(""))
    {
      return null;
    }
    return val;
  }

  /**
   * Obtain a clean version of a text view's contents, stripping any non-numerics 

   * @param view the view

   * @return a clean version of the contents of the text view as only numerics; empty string will be {@code null} rather than ""
   */
  @Nullable
  public static String getCleanNumber(final TextView view)
  {
    return getCleanNumber(view.getText().toString());    
  }

  /**
   * Obtain a clean numeric string given an input string
   * 
   * @param val the input string
   * 
   * @return a clean version of the contents of the string; empty string will be {@code null} rather than ""
   */
  @Nullable
  public static String getCleanNumber(String val)
  {
    if (val != null)
    {
      val = val.replaceAll("[^0-9]", "");
    }
    return (val == null || val.equals("") ? null : val);
  }
}
