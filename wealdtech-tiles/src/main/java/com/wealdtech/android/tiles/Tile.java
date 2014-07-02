package com.wealdtech.android.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.wealdtech.android.R;
import com.wealdtech.android.providers.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public abstract class Tile<T> extends FrameLayout
{
  private static final Logger LOG = LoggerFactory.getLogger(Tile.class);

  /** The width, in tilespaces */
  private int colSpan = 1;
  /** The height, in tilespaces */
  private int rowSpan = 1;

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
    setAttrs(context, attrs, defStyle);
  }

  private void setAttrs(final Context context, final AttributeSet attrs, final int defStyle)
  {
    final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Tile);
    final int N = a.getIndexCount();
    for (int i = 0; i < N; ++i)
    {
      int attr = a.getIndex(i);
      // When we have auto-layout then top and left will go away.
      if (attr == R.styleable.Tile_tile_top)
      {
        params.top = a.getInteger(attr, -1);
      }
      else if (attr == R.styleable.Tile_tile_left)
      {
        params.left= a.getInteger(attr, -1);
      }
      else if (attr == R.styleable.Tile_tile_colspan)
      {
        colSpan = a.getInteger(attr, colSpan);
        params.colSpan = colSpan;
      }
      else if (attr == R.styleable.Tile_tile_rowspan)
      {
        rowSpan = a.getInteger(attr, rowSpan);
        params.rowSpan = rowSpan;
      }
    }
    a.recycle();

    setLayoutParams(params);
    setBackgroundResource(R.drawable.tile_border);
  }

  public void setProvider(final Provider<T> provider)
  {
    this.provider = provider;
    refreshDisplay();
  }

  public abstract void refreshDisplay();

  public static class LayoutParams extends ViewGroup.LayoutParams
  {
    int top = 0;
    int left = 0;
    int colSpan = 1;
    int rowSpan = 1;

    public LayoutParams()
    {
      this(MATCH_PARENT, MATCH_PARENT);
    }

    public LayoutParams(int width, int height)
    {
      super(MATCH_PARENT, MATCH_PARENT);
    }

    public LayoutParams(Context context, AttributeSet attrs)
    {
      super(context, attrs);
      final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Tile);
      top = a.getInt(R.styleable.Tile_tile_top, 0);
      left = a.getInt(R.styleable.Tile_tile_left, 0);
      colSpan = a.getInt(R.styleable.Tile_tile_colspan, 1);
      rowSpan = a.getInt(R.styleable.Tile_tile_rowspan, 1);
      a.recycle();
    }

    public LayoutParams(ViewGroup.LayoutParams params)
    {
      super(params);
    }
  }
}
