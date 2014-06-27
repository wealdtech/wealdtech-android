package com.wealdtech.android.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.WindowManager;

/**
 */
public class TileLayout extends GridLayout
{
  private transient int screenWidth;

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

    setColumnCount(4);
    setRowCount(4);
    final WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

    final Point size = new Point();
    wm.getDefaultDisplay().getSize(size);

    screenWidth = size.x;
  }

  public void addTile(final TileView tileView)
  {
    final Spec colSpec = GridLayout.spec(tileView.x, tileView.width);
    final Spec rowSpec = GridLayout.spec(tileView.y, tileView.height);
    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
    params.width = screenWidth * tileView.width / 4;
    params.height = screenWidth * tileView.height / 4;
    addView(tileView.view, params);
  }
}
