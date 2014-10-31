/*
 * Copyright 2012 - 2014 Weald Technology Trading Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.wealdtech.android.fabric.examples;

import com.wealdtech.android.fabric.FabricActivity;

/**
 * A sample activity showing a number of features of Fabric
 */
public class SampleActivity extends FabricActivity
{
//  static
//  {
//    // Only run this once regardless of how many times we start the activity
//    BasicLogcatConfigurator.configureDefaultContext();
//    final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("com.wealdtech.android.fabric");
//    logger.setLevel(Level.TRACE);
//  }
//
//  public class ViewHolder
//  {
//    public RelativeLayout layout;
//    public TextView textView1;
//    public TextView textView2;
//    public Button button1;
//    public Button button2;
//    public Button button3;
//    public EditText editText1;
//    public EditText editText2;
//  }
//
//  private ViewHolder holder = new ViewHolder();
//
//  public SampleActivity(){}
//
//  @Override
//  public void onCreate(final Bundle savedInstanceState)
//  {
//    super.onCreate(savedInstanceState);
//
//    setContentView(R.layout.play);
//    holder.layout = (RelativeLayout)findViewById(R.id.play_layout);
//
//    holder.textView1 = (TextView)findViewById(R.id.play_text_view_1);
//    holder.textView2 = (TextView)findViewById(R.id.play_text_view_2);
//
//    holder.button1 = (Button)findViewById(R.id.play_button_1);
//
//    holder.button2 = (Button)findViewById(R.id.play_button_2);
//    holder.button3 = (Button)findViewById(R.id.play_button_3);
//    holder.editText1 = (EditText)findViewById(R.id.play_edit_text_1);
//    holder.editText2 = (EditText)findViewById(R.id.play_edit_text_2);
//
//    Integer pressCount;
//    pressCount = Fabric.getInstance().get(this, R.id.play_button_1, "count");
//    if (pressCount == null)
//    {
//      // Don't have it; set it up
//      pressCount = 0;
//      Fabric.getInstance().set(this, R.id.play_button_1, "count", pressCount);
//      Fabric.getInstance().persist(this, R.id.play_button_1, "count");
//    }
//    holder.textView2.setText("Button has been pressed " + pressCount + " time(s)");
//
//    // Rules
//    final Activity thisActivity = this;
//
//    // Increment the click count for button 1
//    when(happens(click(holder.button1))).then(setFabricData(thisActivity, R.id.play_button_1, "count", new Generator<Integer>()
//    {
//      @Override
//      public Integer generate()
//      {
//        return fabric().<Integer>get(thisActivity, R.id.play_button_1, "count") + 1;
//      }
//    }));
//
//    // Example of a custom action using a generator
//    when(happens(change(fabricData(thisActivity, R.id.play_button_1, "count")))).then(setText(holder.textView2,
//                                                                                              new Generator<String>()
//                                                                                              {
//                                                                                                @Override
//                                                                                                public String generate()
//                                                                                                {
//                                                                                                  return "Button has been pressed " + Fabric
//                                                                                                                                          .getInstance()
//                                                                                                                                          .<Integer>get(thisActivity,
//                                                                                                                                                        R.id.play_button_1,
//                                                                                                                                                        "count") + " time(s)";
//                                                                                                }
//                                                                                              }));
//    // Buttons to turn persistence on and off
//    when(happens(click(holder.button2))).then(doAllOf(persist(thisActivity, R.id.play_button_1, "count"),
//                                                      setText(holder.textView1, "Persisting press count")));
//    when(happens(click(holder.button3))).then(doAllOf(unpersist(thisActivity, R.id.play_button_1, "count"),
//                                                      setText(holder.textView1, "Not persisting press count")));
//
//    // Example of long click condition
//    when(happens(longClick(holder.button2))).then(alert(holder.button2, "Ouch"));
//
//    // Validate email when focus is lost on the email field
//    when(happens(focusLost(holder.editText1))).then(validate(holder.button2, emailValidator(), alert(holder.button2, "Valid"),
//                                                             alert(holder.button2, "Invalid")));
//  }
}
