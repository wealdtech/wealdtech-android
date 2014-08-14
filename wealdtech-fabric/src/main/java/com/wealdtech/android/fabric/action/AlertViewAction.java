package com.wealdtech.android.fabric.action;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.wealdtech.android.fabric.FabricDataListener;
import com.wealdtech.android.fabric.condition.ClickHandler;
import com.wealdtech.android.fabric.condition.FocusChangeHandler;
import com.wealdtech.android.fabric.condition.LongClickHandler;

/**
 */
public class AlertViewAction<T> extends ViewAction implements FocusChangeHandler, ClickHandler, LongClickHandler, FabricDataListener<T>
{
  private String text;
  private final View view;

  public AlertViewAction(final View view, final String text)
  {
    super();
    this.view = view;
    this.text = text;
  }

  public static AlertViewAction alert(final View view, final String alert)
  {
    return new AlertViewAction(view, alert);
  }

  @Override
  public View.OnFocusChangeListener getFocusChangeListener()
  {
    return new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(final View v, final boolean hasFocus)
      {
        act();
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
        act();
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
        act();
        return false;
      }
    };
  }

  @Override
  public void act()
  {
    Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDataChanged(final T oldData, final T newData)
  {
    if (oldData != null) {
      if (newData != null)
      {
        text = "Data changed from " + oldData.toString() + " to " + newData.toString();
        act();
      }
      else
      {
        text = "Data now clear";
        act();
      }
    }
    else
    {
      text = "Data now " + newData.toString();
      act();
    }
  }
}
