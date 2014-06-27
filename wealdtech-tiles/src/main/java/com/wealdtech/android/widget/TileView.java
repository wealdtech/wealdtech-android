package com.wealdtech.android.widget;

import android.view.View;

/**
 */
public class TileView
{
  public final View view;
  public final int x;
  public final int y;
  public final int width;
  public final int height;

  public TileView(final View view,
                  final int x,
                  final int y,
                  final int width,
                  final int height)
  {
    this.view = view;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
}
