package com.wealdtech.android.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.wealdtech.android.R;
import com.wealdtech.android.providers.Provider;

/**
 */
public abstract class Tile<T> extends FrameLayout
{
  /** The width, in tilespaces */
  private int width = 1;
  /** The height, in tilespaces */
  private int height = 1;
  /** The scale */
  private int scale = 1;

  /** The provider for the data */
  protected Provider<T> provider;

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

    setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    setBackgroundResource(R.drawable.tile_border);

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

  public void setProvider(final Provider<T> provider)
  {
    this.provider = provider;
    refreshDisplay();
  }

  public void setScale(final int scale)
  {
    this.scale = scale;
  }

  public abstract void refreshDisplay();
}
