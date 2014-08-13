package com.wealdtech.android.fabric.definition;

import android.view.View;
import com.wealdtech.android.fabric.condition.ViewCondition;

/**
 */
public class ViewDefinition
{
  public final View view;

  public ViewDefinition(final View view)
  {
    this.view = view;
  }

  public ViewCondition encounters(ViewCondition condition)
  {
    condition.definition = this;
    return condition;
  }
}
