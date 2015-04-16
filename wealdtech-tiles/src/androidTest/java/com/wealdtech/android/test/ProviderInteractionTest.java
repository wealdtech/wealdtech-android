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
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.wealdtech.android.providers.Provider;
import com.wealdtech.android.test.providers.PresetTextProvider;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

//@RunWith(AndroidJUnit4.class)
//@SmallTest
public class ProviderInteractionTest extends ActivityInstrumentationTestCase2<ProviderInteractionTestActivity>
{
  private static final Logger LOG = LoggerFactory.getLogger(ProviderInteractionTest.class);

  public ProviderInteractionTest()
  {
    super(ProviderInteractionTestActivity.class);
  }

  @Before
  public void setUp() throws Exception
  {
    BasicLogcatConfigurator.configureDefaultContext();
    super.setUp();
    getActivity();
  }

  // Ensure that we can create a provider, set a listener then start providing
//  @Test
  public void testProviderOrderingListenerFirst()
  {
    final Provider<String> provider = new PresetTextProvider("testProviderOrderingListenerFirst");

    final ProviderInteractionTestActivity activity = getActivity();

    provider.addDataChangedListener(activity.holder.textTile1);

//    onView(withId(activity.holder.textTile1.holder.text.getId())).check(matches(withText("")));
//    assertThat(activity.holder.textTile1.holder.text.getText().toString(), equalTo(""));

    provider.startProviding();

    // Sleep to let the provider do its work
    try{ Thread.sleep(500L); } catch (final InterruptedException ignored){}

    // Need to stop providing again otherwise Espresso won't continue
    provider.stopProviding();

    onView(withId(activity.holder.textTile1.holder.text.getId())).check(matches(withText("testProviderOrderingListenerFirst")));
//    onView(withId(activity.holder.textTile1.holder.text.getId())).check(matches(withText("testProviderOrderingListenerFirst")));

    activity.finish();
  }

  // Ensure that we can create a provider, set a listener then start providing
//  @Test
  public void testProviderOrderingProvidingFirst()
  {
    final Provider<String> provider = new PresetTextProvider("testProviderOrderingProvidingFirst");

    final ProviderInteractionTestActivity activity = getActivity();

    provider.startProviding();

//    onView(withId(activity.holder.textTile1.holder.text.getId())).check(matches(withText("")));
//    assertThat(activity.holder.textTile1.holder.text.getText(), is(""));

    provider.addDataChangedListener(activity.holder.textTile1);

    // Sleep to let the provider do its work
    try{ Thread.sleep(500L); } catch (final InterruptedException ignored){}

//    onView(withId(activity.holder.textTile1.holder.text.getId())).check(matches(withText("testProviderOrderingProvidingFirst")));
//    assertThat(activity.holder.textTile1.holder.text.getText().toString(), equalTo("testProviderOrderingProvidingFirst"));

    provider.stopProviding();

    onView(withId(activity.holder.textTile1.holder.text.getId())).check(matches(withText("testProviderOrderingProvidingFirst")));
//    onView(withId(activity.holder.textTile1.holder.text.getId())).check(matches(withText("testProviderOrderingProvidingFirst")));

    activity.finish();
  }
}
