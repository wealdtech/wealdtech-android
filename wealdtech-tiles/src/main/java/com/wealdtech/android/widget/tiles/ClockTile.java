package com.wealdtech.android.widget.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

/**
 * A tile which shows a clock
 */
public class ClockTile extends TileView<Date> implements DataChangedListener
{
  @Override
  public void onDataChanged()
  {
    refreshDisplay();
  }

  private static class ViewHolder
  {
    public TextView text;
  }
  private final ViewHolder holder = new ViewHolder();

  public ClockTile(final Context context)
  {
    this(context, null, 0);
  }

  public ClockTile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public ClockTile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);
    this.width = 1;
    this.height = 1;

    this.view = new LinearLayout(context);

    this.holder.text = new TextView(context);

    this.view.addView(this.holder.text);
  }

  @Override
  public void refreshDisplay()
  {
    this.holder.text.setText(provider.getData().toString());
  }
}
