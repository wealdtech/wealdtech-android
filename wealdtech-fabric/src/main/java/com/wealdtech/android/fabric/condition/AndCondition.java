/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.condition;

import com.google.common.collect.ImmutableSet;

/**
 */
public class AndCondition extends Condition
{
  private final ImmutableSet<Condition> conditions;

  private AndCondition(final Condition... conditions)
  {
    this.conditions = ImmutableSet.copyOf(conditions);
  }

  @Override
  public boolean isMet()
  {
    for (final Condition condition : conditions)
    {
      if (!condition.isMet())
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Compound condition which is met if all included conditions are met
   */
  public static Condition allOf(Condition... conditions)
  {
    return new AndCondition(conditions);
  }
}
