package com.wealdtech.android.test.tiles;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wealdtech.android.providers.DataChangedListener;
import com.wealdtech.android.tiles.Tile;
import com.wealdtech.android.utils.ViewUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile which shows simple text
 */
public class TextTile extends Tile<String> implements DataChangedListener<String>
{
  private static final Logger LOG = LoggerFactory.getLogger(TextTile.class);

  private static final Editable EDITABLE = Editable.NEVER;
  private static final boolean EXPANDABLE = true;

  public static class ViewHolder
  {
    public TextView text;
  }

  public final ViewHolder holder = new ViewHolder();

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
    super(context, attrs, defStyle, EDITABLE, EXPANDABLE);

    holder.text = new TextView(context);
    holder.text.setId(ViewUtils.generateViewId());
    holder.text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                           ViewGroup.LayoutParams.MATCH_PARENT));

    addView(holder.text);
  }

  @Override
  public void onDataChanged(final String data)
  {
    refreshDisplay(data);
  }

  @Override
  protected void refreshDisplay(final String data)
  {
    holder.text.setText(data);
  }

  @Override
  public void onTileExpanded()
  {
    holder.text.setTextColor(Color.BLUE);
  }

  @Override
  public void onTileContracted()
  {
    holder.text.setTextColor(Color.WHITE);
  }
}