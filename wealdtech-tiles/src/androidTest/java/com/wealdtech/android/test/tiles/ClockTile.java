package com.wealdtech.android.test.tiles;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import com.wealdtech.android.providers.DataChangedListener;
import com.wealdtech.android.tiles.Tile;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * A tile which shows a clock
 */
public class ClockTile extends Tile<Date> implements DataChangedListener<Date>
{
  private static final Logger LOG = LoggerFactory.getLogger(ClockTile.class);

  private static final Editable EDITABLE = Editable.NEVER;
  private static final boolean EXPANDABLE = true;

  public static class ViewHolder
  {
    public Button display;
  }
  public final ViewHolder holder = new ViewHolder();

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
    super(context, attrs, defStyle, EDITABLE, EXPANDABLE);

    holder.display = new Button(context);
    holder.display.setId(ViewUtils.generateViewId());
    holder.display.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                              ViewGroup.LayoutParams.MATCH_PARENT));
    holder.display.setBackgroundColor(Color.RED);
    addView(holder.display);
  }

  @Override
  public void onDataChanged(final Date data)
  {
    refreshDisplay(data);
  }

  @Override
  protected void refreshDisplay(final Date data)
  {
    if (data != null)
    {
      holder.display.setText(data.toString());
    }
      else
    {
      holder.display.setText(null);
    }
  }
}
