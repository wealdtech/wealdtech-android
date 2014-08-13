package com.wealdtech.android.fabric.action;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.wealdtech.android.fabric.condition.ClickHandler;
import com.wealdtech.android.fabric.condition.FocusChangeHandler;
import com.wealdtech.android.fabric.condition.LongClickHandler;

/**
 */
public class AlertViewAction implements ViewAction, FocusChangeHandler, ClickHandler, LongClickHandler
{
  final String text;

  public AlertViewAction(final String text)
  {
    this.text = text;
  }

  public static AlertViewAction alert(final String alert)
  {
    return new AlertViewAction(alert);
  }

  @Override
  public View.OnFocusChangeListener getFocusChangeListener(final View view)
  {
    return new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(final View v, final boolean hasFocus)
      {
        act(v);
      }
    };
  }

  @Override
  public Button.OnClickListener getClickListener(final View view)
  {
    return new Button.OnClickListener(){
      @Override
      public void onClick(final View v)
      {
        act(v);
      }
    };
  }

  @Override
  public Button.OnLongClickListener getLongClickListener(final View view)
  {
    return new Button.OnLongClickListener(){
      @Override
      public boolean onLongClick(final View v)
      {
        act(v);
        return false;
      }
    };
  }

  @Override
  public void act(final View view)
  {
    Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
  }
}
