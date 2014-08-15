package com.wealdtech.android.fabric.validator;

import android.view.View;
import android.widget.TextView;

/**
 */
public class PresentValidator extends TextValidator
{
  private static PresentValidator instance;

  private PresentValidator()
  {
    super();
  }

  @Override
  public boolean validate(final View view)
  {
    final String val = ((TextView)view).getText().toString().trim();
    return val.length() > 0;
  }

  /**
   * A validator which validates that there is some actual text present
   */
  public static PresentValidator presentValidator()
  {
    if (instance == null)
    {
      synchronized (PresentValidator.class)
      {
        // Double check
        if (instance == null)
        {
          instance = new PresentValidator();
        }
      }
    }
    return instance;
  }
}
