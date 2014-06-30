package com.wealdtech.android.widget.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 */
public abstract class TileView<T> extends View
{
  /** The X ratio */
  protected int width;
  /** The Y ratio */
  protected int height;

  /** The view containing the data */
  protected ViewGroup view;

  /** The provider for the data */
  protected TileProvider<T> provider;

  public TileView(final Context context)
  {
    this(context, null, 0);
  }

  public TileView(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public TileView(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);
  }

  public ViewGroup getViewGroup()
  {
    return this.view;
  }

  public void setProvider(final TileProvider<T> provider)
  {
    this.provider = provider;
  }

  public abstract void refreshDisplay();
}
