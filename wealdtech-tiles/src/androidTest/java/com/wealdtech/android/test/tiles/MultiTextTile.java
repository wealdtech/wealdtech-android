package com.wealdtech.android.test.tiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.wealdtech.android.tiles.DataChangedListener;
import com.wealdtech.android.tiles.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tile which shows text
 */
public class MultiTextTile extends Tile<String> implements DataChangedListener<String>
{
  private static final Logger LOG = LoggerFactory.getLogger(MultiTextTile.class);

  private static final Editable EDITABLE = Editable.NEVER;
  private static final boolean EXPANDABLE = true;

  private static class ViewHolder
  {
    public TextView text1;
    public TextView text2;
    public TextView text3;
    public TextView text4;
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
    super(context, attrs, defStyle, EDITABLE, EXPANDABLE);

    // FIXME build system is not picking up the correct R; need to find out what is going on and fix it
    final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    inflater.inflate(R.layout.test_multi_text, this, true);
//    holder.text1 = (TextView)findViewById(R.id.test_multi_text_1);
//    holder.text2 = (TextView)findViewById(R.id.test_multi_text_2);
//    holder.text3 = (TextView)findViewById(R.id.test_multi_text_3);
//    holder.text4 = (TextView)findViewById(R.id.test_multi_text_4);
    holder.text1 = new TextView(getContext());
    holder.text2 = new TextView(getContext());
    holder.text3 = new TextView(getContext());
    holder.text4 = new TextView(getContext());
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
      holder.text3.setText(provider.getData());
      holder.text4.setText(provider.getData());
    }
    else
    {
      holder.text1.setText(null);
      holder.text2.setText(null);
      holder.text3.setText(null);
      holder.text4.setText(null);
    }
  }

  @Override
  public boolean willShowInformation()
  {
    return provider.getData() != null;
  }
}
