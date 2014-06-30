package com.wealdtech.android.widget.tiles;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.WindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class TileLayout extends GridLayout
{
  private static final Logger LOG = LoggerFactory.getLogger(TileLayout.class);

  private transient int screenWidth;

  private int curWidth = 1;
  private int curHeight = 1;

  public TileLayout(final Context context)
  {
    this(context, null, 0);
  }

  public TileLayout(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public TileLayout(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);
    initLayout(context);
  }

  private void initLayout(final Context context)
  {
    LOG.info("initLayout()");

    setColumnCount(4);
    setRowCount(4);
    final WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

    final Point size = new Point();
    wm.getDefaultDisplay().getSize(size);

    screenWidth = size.x;
  }

  public void addTile(final TileView tile)
  {
    LOG.info("addTile()");

    final Spec rowSpec = GridLayout.spec(curHeight, tile.height);
    final Spec colSpec = GridLayout.spec(curWidth++, tile.width);
    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
    params.width = screenWidth * tile.width / 4;
    params.height = screenWidth * tile.height / 4;
    addView(tile.view, params);
  }
}
