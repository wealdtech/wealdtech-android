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
