package com.wealdtech.android.widget.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.wealdtech.android.R;

/**
 */
public abstract class Tile<T> extends View
{
  /** The width, in tilespaces */
  private int width = 1;
  /** The height, in tilespaces */
  private int height = 1;
  /** The scale */
  private int scale = 1;

  /** The view containing the data */
  protected ViewGroup view;

  /** The provider for the data */
  protected TileProvider<T> provider;

  public Tile(final Context context)
  {
    this(context, null, 0);
  }

  public Tile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public Tile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);
    final TypedArray a = context.obtainStyledAttributes(attrs,
                                                  R.styleable.Tile);

    final int N = a.getIndexCount();
    for (int i = 0; i < N; ++i)
    {
      int attr = a.getIndex(i);
      if (attr == R.styleable.Tile_scale)
      {
        setScale(a.getInteger(attr, 1));
      }
    }
    a.recycle();

  }

  public void setTileRatio(final int width, final int height)
  {
    this.width = width;
    this.height = height;
  }

  public int getTileWidth()
  {
    return width * scale;
  }

  public int getTileHeight()
  {
    return height * scale;
  }

  public ViewGroup getViewGroup()
  {
    return this.view;
  }

  public void setProvider(final TileProvider<T> provider)
  {
    this.provider = provider;
  }

  public void setScale(final int scale)
  {
    this.scale = scale;
  }

  public abstract void refreshDisplay();
}
