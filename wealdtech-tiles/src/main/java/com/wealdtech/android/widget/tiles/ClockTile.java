package com.wealdtech.android.widget.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * A tile which shows a clock
 */
public class ClockTile extends Tile<Date> implements DataChangedListener
{
  private static final Logger LOG = LoggerFactory.getLogger(ClockTile.class);

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

    LOG.error("Instantiating clock tile");
    setTileRatio(1, 1);

    view = new LinearLayout(context);
    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                              ViewGroup.LayoutParams.MATCH_PARENT));

    holder.display = new Button(context);
    holder.display.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));

    view.addView(holder.display);
  }

  @Override
  public void onDataChanged()
  {
    refreshDisplay();
  }

  @Override
  public void refreshDisplay()
  {
    holder.display.setText(provider.getData().toString());
  }
}
