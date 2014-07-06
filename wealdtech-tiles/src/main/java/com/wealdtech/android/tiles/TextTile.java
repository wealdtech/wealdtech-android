package com.wealdtech.android.tiles;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile which shows text
 */
public class TextTile extends Tile<String> implements DataChangedListener<String>
{
  private static final Logger LOG = LoggerFactory.getLogger(TextTile.class);

  private static final Editable EDITABLE = Editable.NEVER;
  private static final boolean EXPANDABLE = true;

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
    super(context, attrs, defStyle, EDITABLE, EXPANDABLE);

    holder.display = new Button(context);
    holder.display.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                              ViewGroup.LayoutParams.MATCH_PARENT));

    addView(holder.display);
  }

  @Override
  public void onDataChanged(final String data)
  {
    refreshDisplay();
  }

  @Override
  public void refreshDisplay()
  {
    if (provider != null && provider.getData() != null)
    {
      holder.display.setText(provider.getData());
    }
    else
    {
      holder.display.setText(null);
    }
  }

  @Override
  public void onTileExpanded()
  {
    holder.display.setTextColor(Color.BLUE);
  }

  @Override
  public void onTileContracted()
  {
    holder.display.setTextColor(Color.WHITE);
  }

  @Override
  public boolean willShowInformation()
  {
    return provider.getData() != null;
  }
}
