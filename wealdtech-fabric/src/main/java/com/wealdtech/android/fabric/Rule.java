/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric;

import com.wealdtech.android.fabric.action.Action;
import com.wealdtech.android.fabric.condition.Condition;
import com.wealdtech.android.fabric.condition.TrueCondition;
import com.wealdtech.android.fabric.trigger.Trigger;

import static com.wealdtech.Preconditions.checkState;

/**
 */
public class Rule
{
  Trigger trigger;
  Condition condition;
  public Action action;
  public Action negativeAction;

  public void act()
  {
    if (condition.isMet())
    {
      action.act(this);
    }
    else
    {
      if (negativeAction != null)
      {
        negativeAction.act(this);
      }
    }
  }

  public static Rule when(Trigger trigger)
  {
    Rule dta = new Rule();
    dta.trigger = trigger;
    return dta;
  }

  public Rule then(Action action)
  {
    this.action = action;
    checkState(trigger != null, "Cannot set up a rule without a trigger");
    if (condition == null)
    {
      condition = new TrueCondition();
    }
    checkState(action != null, "Cannot set up a rule without an action");
    trigger.setUp(this);
    return this;
  }

  public void otherwise(Action action)
  {
    this.negativeAction = action;
  }

  public Rule and(final Condition condition)
  {
    this.condition = condition;
    return this;
  }
}
