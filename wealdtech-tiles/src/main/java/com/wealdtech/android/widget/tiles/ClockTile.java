package com.wealdtech.android.widget.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * A tile which shows a clock
 */
public class ClockTile extends TileView<Date> implements DataChangedListener
{
  private static final Logger LOG = LoggerFactory.getLogger(ClockTile.class);

  @Override
  public void onDataChanged()
  {
    refreshDisplay();
  }

  private static class ViewHolder
  {
    public Button display;
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

    this.holder.display = new Button(context);

    this.view.addView(this.holder.display);
  }

  @Override
  public void refreshDisplay()
  {
    LOG.info("refreshDisplay()");
    this.holder.display.setText(provider.getData().toString());
  }
}
