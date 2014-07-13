package com.wealdtech.android.test.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wealdtech.android.providers.DataChangedListener;
import com.wealdtech.android.tiles.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * A tile which changes which layout it is displaying information in depending on an internal counter
 */
public class FlipTile extends Tile<Date> implements DataChangedListener<Date>
{
  private static final Logger LOG = LoggerFactory.getLogger(FlipTile.class);

  private int counter = 0;

  private static class ViewHolder
  {
    public LinearLayout layout;
    public TextView text;
    public TextView text2;
    public TextView text3;
  }
  private final ViewHolder holder = new ViewHolder();

  public FlipTile(final Context context)
  {
    this(context, null);
  }

  public FlipTile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public FlipTile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);

    holder.layout = new LinearLayout(context);
    holder.layout.setOrientation(LinearLayout.HORIZONTAL);
    addView(holder.layout);

    holder.text = new TextView(context);
    holder.text.setTextSize(24);
    holder.text.setText("1");
    holder.layout.addView(holder.text);

    holder.text2 = new TextView(context);
    holder.text2.setTextSize(32);
    holder.text2.setText("|");
    holder.layout.addView(holder.text2);

    holder.text3 = new TextView(context);
    holder.text3.setTextSize(24);
    holder.text3.setText("3");
    holder.layout.addView(holder.text3);
  }

  @Override
  public void onDataChanged(final Date date)
  {
    refreshDisplay(date);
  }

  @Override
  public void refreshDisplay(final Date date)
  {
    LOG.error("Setting display to {} (counter is {})", date.toString(), counter);
    if (counter % 2 == 0)
    {
      holder.text.setVisibility(View.INVISIBLE);

     holder.text3.setText(date.toString());
      holder.text3.setVisibility(View.VISIBLE);
    }
    else
    {
      holder.text3.setVisibility(View.INVISIBLE);

      holder.text.setText(date.toString());
      holder.text.setVisibility(View.VISIBLE);
    }
    counter++;
  }
}
