package com.wealdtech.android.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.wealdtech.android.R;
import com.wealdtech.android.providers.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public abstract class Tile<T> extends FrameLayout
{
  private static final Logger LOG = LoggerFactory.getLogger(Tile.class);

  /** The provider for the data */
  protected Provider<T> provider;

  /** The controls for the tile */
  protected final View controlLayout;

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
    controlLayout = initControlView(context);
  }

  private View initControlView(final Context context)
  {
    final TextView controlView;
    if (hasControls())
    {
      controlView = new TextView(context);
      controlView.setText("+");
      controlView.setBackgroundColor(Color.TRANSPARENT);
      controlView.setTextColor(Color.GREEN);
      controlView.setTextSize(24);

      controlView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                               ViewGroup.LayoutParams.WRAP_CONTENT,
                                                               Gravity.BOTTOM | Gravity.RIGHT));
      addView(controlView);
    }
    else
    {
      // No controls
      controlView = null;
    }
    return controlView;
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
        params.colSpan = a.getInteger(attr, params.colSpan);
      }
      else if (attr == R.styleable.Tile_tile_rowspan)
      {
        params.rowSpan = a.getInteger(attr, params.rowSpan);
      }
      else if (attr == R.styleable.Tile_tile_expandable)
      {
        params.expandable = a.getBoolean(attr, params.expandable);
      }
      else if (attr == R.styleable.Tile_tile_editable)
      {
        params.editable = a.getBoolean(attr, params.editable);
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

  public void onTileExpanded()
  {
  }

  public void onTileContracted()
  {
  }

  public boolean hasControls()
  {
    final LayoutParams params = (Tile.LayoutParams)getLayoutParams();
    return params.expandable || params.editable;
  }

  public static class LayoutParams extends ViewGroup.LayoutParams
  {
    public int top = -1;
    public int left = -1;
    public int colSpan = 1;
    public int rowSpan = 1;
    public boolean expandable = true;
    public boolean editable = false;

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
      top = a.getInt(R.styleable.Tile_tile_top, top);
      left = a.getInt(R.styleable.Tile_tile_left, left);
      colSpan = a.getInt(R.styleable.Tile_tile_colspan, colSpan);
      rowSpan = a.getInt(R.styleable.Tile_tile_rowspan, rowSpan);
      expandable = a.getBoolean(R.styleable.Tile_tile_expandable, expandable);
      editable = a.getBoolean(R.styleable.Tile_tile_editable, editable);
      a.recycle();
    }

    public LayoutParams(ViewGroup.LayoutParams params)
    {
      super(params);
      if (params instanceof LayoutParams)
      {
        top = ((LayoutParams)params).top;
        left = ((LayoutParams)params).left;
        colSpan = ((LayoutParams)params).colSpan;
        rowSpan = ((LayoutParams)params).rowSpan;
        expandable = ((LayoutParams)params).expandable;
        editable = ((LayoutParams)params).editable;
      }
    }
  }

  @Override
  public void addView(final View view)
  {
    super.addView(view);
    if (controlLayout != null && controlLayout != view)
    {
      controlLayout.bringToFront();
    }
  }
}
