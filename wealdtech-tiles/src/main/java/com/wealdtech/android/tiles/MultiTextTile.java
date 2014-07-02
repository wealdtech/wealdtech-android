package com.wealdtech.android.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.wealdtech.android.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile which shows text
 */
public class MultiTextTile extends Tile<String> implements DataChangedListener<String>
{
  private static final Logger LOG = LoggerFactory.getLogger(MultiTextTile.class);

  private static class ViewHolder
  {
    public TextView text1;
    public TextView text2;
  }
  private final ViewHolder holder = new ViewHolder();

  public MultiTextTile(final Context context)
  {
    this(context, null, 0);
  }

  public MultiTextTile(final Context context, final AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public MultiTextTile(final Context context, final AttributeSet attrs, final int defStyle)
  {
    super(context, attrs, defStyle);

    setTileRatio(1, 1);

    final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.test_multi_text, this, true);
    holder.text1 = (TextView)findViewById(R.id.test_multi_text_1);
    holder.text2 = (TextView)findViewById(R.id.test_multi_text_2);
  }

  @Override
  public void onDataChanged(final String data)
  {
    refreshDisplay();
  }

  @Override
  public void refreshDisplay()
  {
    if (provider != null &&
        provider.getData() != null)
    {
      holder.text1.setText(provider.getData());
      holder.text2.setText(provider.getData());
    }
    else
    {
      holder.text1.setText(null);
      holder.text2.setText(null);
    }
  }
}