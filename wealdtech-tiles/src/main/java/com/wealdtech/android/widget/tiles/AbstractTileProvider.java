package com.wealdtech.android.widget.tiles;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public abstract class AbstractTileProvider<T> implements TileProvider<T>
{
  private static final Logger LOG = LoggerFactory.getLogger(AbstractTileProvider.class);

  private final List<DataChangedListener> listeners = new ArrayList<>();

  private T data;

  public T getData()
  {
    return data;
  }

  protected void setData(final T data)
  {
    this.data = data;
  }

  @Override
  public void addDataChangedListener(final DataChangedListener listener)
  {
    listeners.add(listener);
  }

  protected void notifyListeners()
  {
    for (final DataChangedListener listener : listeners)
    {
      listener.onDataChanged();
    }
  }
}
