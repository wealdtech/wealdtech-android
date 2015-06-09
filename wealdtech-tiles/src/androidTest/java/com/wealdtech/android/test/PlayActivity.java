/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.R;
import com.wealdtech.android.tiles.SimpleTextTile;
import com.wealdtech.android.tiles.TileLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class PlayActivity extends Activity
{
//  private static final Logger LOG = LoggerFactory.getLogger(PlayActivity.class);

//  static
//  {
//    // Only run this once regardless of how many times we start the activity
//    BasicLogcatConfigurator.configureDefaultContext();
//  }

  public PlayActivity()
  {
  }

//  private AsyncTask<Void, Void, Void> visibilityTask;

//  public class ViewHolder
//  {
//    public TileLayout layout;
//    public SimpleTextTile textTile1;
//    public SimpleTextTile textTile2;
//    public SimpleTextTile textTile3;
//    public SimpleTextTile textTile4;
//  }
//  private ViewHolder holder = new ViewHolder();

//  @Override
//  public void onCreate(final Bundle savedInstanceState)
//  {
//    super.onCreate(savedInstanceState);
//
//    setContentView(R.layout.play);
//    holder.layout = (TileLayout)findViewById(R.id.play_layout);
//
//    holder.textTile1 = (SimpleTextTile)findViewById(R.id.play_text_tile_1);
//    holder.textTile1.onDataChanged("Tile 1");
//    holder.textTile2 = (SimpleTextTile)findViewById(R.id.play_text_tile_2);
//    holder.textTile2.onDataChanged("Tile 2");
//    holder.textTile3 = (SimpleTextTile)findViewById(R.id.play_text_tile_3);
//    holder.textTile3.onDataChanged("Tile 3");
//    holder.textTile4 = (SimpleTextTile)findViewById(R.id.play_text_tile_4);
//    holder.textTile4.onDataChanged("Tile 4");
//
//    LOG.error("onCreate()");
//    visibilityTask = new AsyncTask<Void, Void, Void>()
//    {
//      int count = 0;
//      @Override
//      protected Void doInBackground(final Void... params)
//      {
//        while (!isCancelled())
//        {
//          publishProgress();
//          try
//          {
//            Thread.sleep(5000L);
//          }
//          catch (final InterruptedException ignored)
//          {
//          }
//        }
//        return null;
//      }
//
//      @Override
//      protected void onProgressUpdate(final Void... item)
//      {
//        if (count++ % 2 == 0)
//        {
//          LOG.error("Setting visibility of first tile to GONE");
//          holder.textTile1.setVisibility(View.GONE);
//        }
//        else
//        {
//          LOG.error("Setting visibility of first tile to VISIBLE");
//          holder.textTile1.setVisibility(View.VISIBLE);
//        }
//        holder.textTile1.requestLayout();
//      }
//    };
//    visibilityTask.execute(null, null, null);
//  }

//  @Override
//  protected void onDestroy()
//  {
//    super.onDestroy();
//    visibilityTask.cancel(true);
//  }
}
