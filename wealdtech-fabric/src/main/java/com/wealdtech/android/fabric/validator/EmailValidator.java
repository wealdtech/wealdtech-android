package com.wealdtech.android.fabric.validator;

import android.view.View;
import android.widget.TextView;

/**
 */
public class EmailValidator extends TextValidator
{
  private static EmailValidator instance;

  private EmailValidator()
  {
    super();
  }

  @Override
  public boolean validate(final View view)
  {
    final String val = ((TextView)view).getText().toString().trim();
    return val.contains("@") && (val.indexOf("@") != 0) && (val.indexOf("@") != val.length() - 1);
  }

  /**
   * Validate that the view contains something which could be considered an email address.
   * Due to the complexity of email addresses all this does is check for the presence of @ with some text either side
   */
  public static EmailValidator emailValidator()
  {
    if (instance == null)
    {
      synchronized (EmailValidator.class)
      {
        // Double check
        if (instance == null)
        {
          instance = new EmailValidator();
        }
      }
    }
    return instance;
  }
}
