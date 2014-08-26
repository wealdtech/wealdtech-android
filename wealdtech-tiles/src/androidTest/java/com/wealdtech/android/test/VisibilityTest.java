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

import android.test.ActivityInstrumentationTestCase2;
import com.wealdtech.android.providers.CachingProvider;
import com.wealdtech.android.providers.Provider;
import com.wealdtech.android.test.providers.DateProvider;
import com.wealdtech.android.test.providers.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class VisibilityTest extends ActivityInstrumentationTestCase2<VisibilityTestActivity>
{
  private static final Logger LOG = LoggerFactory.getLogger(VisibilityTest.class);

  private final Provider<Date> dateProvider;
  private final Provider<String> cachedProvider1;
  private final Provider<String> cachedProvider2;

  public VisibilityTest()
  {
    super(VisibilityTestActivity.class);
    dateProvider = new DateProvider();
    final TimeProvider timeProvider1 = new TimeProvider();
    dateProvider.addDataChangedListener(timeProvider1);
    final TimeProvider timeProvider2 = new TimeProvider();
    dateProvider.addDataChangedListener(timeProvider2);
    cachedProvider1 = new CachingProvider<>(timeProvider1, 2000L);
    cachedProvider2 = new CachingProvider<>(timeProvider2, 2000L);
    dateProvider.startProviding();
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    setActivityInitialTouchMode(false);
  }

  public void testLayout()
  {
    final VisibilityTestActivity activity = getActivity();
//    dateProvider.addDataChangedListener(activity.holder.flipTile);

    cachedProvider1.addDataChangedListener(activity.holder.textTile1);
    cachedProvider1.startProviding();
    cachedProvider2.startProviding();

    try { Thread.sleep(500L); } catch (final InterruptedException ignored) {}
    cachedProvider1.addDataChangedListener(activity.holder.textTile2);
    try { Thread.sleep(500L); } catch (final InterruptedException ignored) {}
    cachedProvider2.addDataChangedListener(activity.holder.textTile3);
    try { Thread.sleep(500L); } catch (final InterruptedException ignored) {}
    cachedProvider2.addDataChangedListener(activity.holder.textTile4);

    try { Thread.sleep(20000L); } catch (final InterruptedException ignored) {}

    activity.finish();
  }
//  public void testNull()
//  {
//    final TileLayoutTestActivity activity = getActivity();
//    activity.finish();
//  }
//
//  public void testEspresso()
//  {
//    final TileLayoutTestActivity activity = getActivity();
////    onView(withId(R.id.test_layout_text_tile_1)).check(matches(withText("Text view 1")));
//    activity.finish();
//  }
}
