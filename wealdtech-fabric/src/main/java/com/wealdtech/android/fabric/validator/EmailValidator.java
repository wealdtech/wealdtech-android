package com.wealdtech.android.fabric.validator;

import android.view.View;
import android.widget.TextView;
import com.wealdtech.ServerError;

/**
 */
public class EmailValidator implements Validator
{
  @Override
  public boolean validate(final View view)
  {
    if (view instanceof TextView)
    {
      return ((TextView)view).getText().toString().contains("@");
    }
    throw new ServerError("Invalid view for email validator");
  }

  public static EmailValidator emailValidator()
  {
    // FIXME should return instance
    return new EmailValidator();
  }
}
