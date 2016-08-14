package com.wealdtech.android;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.wealdtech.android.fabric.Rule;
import com.wealdtech.android.fabric.trigger.ViewTrigger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A view that provides a date display in a button with the ability to alter via the DatePicker dialog fragment
 */
public class DateView extends Button implements DatePickerDialog.OnDateSetListener
{
  // Listeners for changes to the date
  private List<OnDateChangedListener> listeners = new ArrayList<>();

  // Local storage for the date
  private LocalDate date;

  public DateView(final Context context)
  {
    super(context);
    init();
  }

  public DateView(final Context context, final AttributeSet attrs)
  {
    super(context, attrs);
    init();
  }

  public DateView(final Context context, final AttributeSet attrs, final int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DateView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
  {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  /**
   * Add a listener for changes to the date
   * @param listener the listener that will receive notification of changes to the date
   */
  public void addOnDateChangedListener(final OnDateChangedListener listener)
  {
    listeners.add(listener);
  }

  public void removeOnDateChangedListener(final OnDateChangedListener listener)
  {
    listeners.remove(listener);
  }

  private void init()
  {
    // Default to today's date
    setDate(LocalDate.now());

    // Set our onClick listener
    super.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(final View view)
      {
        new DatePickerDialog(getContext(), DateView.this, date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth()).show();
      }
    });
  }

  /**
   * @return the date
   */
  public LocalDate getDate(){ return date; }

  /**
   * Set the date
   * @param date the date
   */
  public void setDate(final LocalDate date)
  {
    this.date = date;
    setText(DateTimeFormat.longDate().withLocale(Locale.getDefault()).print(date));
    for (final OnDateChangedListener listener : listeners)
    {
      listener.onDateChanged(date);
    }
  }

  @Override
  public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day)
  {
    setDate(date.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day));
  }

  public interface OnDateChangedListener
  {
    void onDateChanged(LocalDate date);
  }

  @Override
  public Parcelable onSaveInstanceState()
  {
    final Bundle bundle = new Bundle();
    bundle.putParcelable("superState", super.onSaveInstanceState());
    bundle.putInt("year", date.getYear());
    bundle.putInt("month", date.getMonthOfYear());
    bundle.putInt("day", date.getDayOfMonth());
    return bundle;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state)
  {
    if (state instanceof Bundle)
    {
      Bundle bundle = (Bundle)state;
      if (bundle.containsKey("year") && bundle.containsKey("month") && bundle.containsKey("day"))
      {
        setDate(new LocalDate(bundle.getInt("year"), bundle.getInt("month"), bundle.getInt("day")));
      }
      state = bundle.getParcelable("superState");
    }
    super.onRestoreInstanceState(state);
  }

  public static class DateChangeViewTrigger extends ViewTrigger
  {
    public DateChangeViewTrigger(final DateView view)
    {
      super(view);
    }

    private OnDateChangedListener listener = null;

    @Override
    public void setUp(final Rule dta)
    {
      listener = new OnDateChangedListener()
      {
        @Override
        public void onDateChanged(final LocalDate date)
        {
          dta.act();
        }
      };

      ((DateView)view).addOnDateChangedListener(listener);
    }

    void tearDown()
    {
      if (listener != null)
      {
        ((DateView)view).removeOnDateChangedListener(listener);
      }
    }

    public static DateChangeViewTrigger dateChanges(final DateView view)
    {
      return new DateChangeViewTrigger(view);
    }
  }
}
