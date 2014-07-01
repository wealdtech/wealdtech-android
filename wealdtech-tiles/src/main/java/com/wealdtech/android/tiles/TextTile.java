package com.wealdtech.android.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile which shows text
 */
public class TextTile extends Tile<String> implements DataChangedListener
{
  private static final Logger LOG = LoggerFactory.getLogger(TextTile.class);

  private static class ViewHolder
  {
    public Button display;
  }
  private final ViewHolder holder = new ViewHolder();

  public TextTile(final Context context)
  {
    this(context, null, 0);
  }

  public TextTile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public TextTile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);

    setTileRatio(2, 1);

    setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    holder.display = new Button(context);
    holder.display.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));

    addView(holder.display);
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
