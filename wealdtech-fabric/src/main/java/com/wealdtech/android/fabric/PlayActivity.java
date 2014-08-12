package com.wealdtech.android.fabric;

import android.app.Activity;

/**
 */
public class PlayActivity extends Activity
{
//  private static final Logger LOG = LoggerFactory.getLogger(Fabric.class);
//
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
//  }
//  private ViewHolder holder = new ViewHolder();
//
//  public PlayActivity()
//  {
//
//  }
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
//    final Activity thisActivity = this;
//    holder.button1 = (Button)findViewById(R.id.play_button_1);
//    holder.button1.setOnClickListener(new View.OnClickListener()
//    {
//      @Override
//      public void onClick(final View v)
//      {
//        Fabric.getInstance().set(thisActivity, R.id.play_button_1, "count", Fabric.getInstance().<Integer>get(thisActivity, R.id.play_button_1, "count") + 1);
//        holder.textView2.setText("Button has been pressed " + Fabric.getInstance().get(thisActivity, R.id.play_button_1, "count") + " times");
//      }
//    });
//
//    holder.button2 = (Button)findViewById(R.id.play_button_2);
//    holder.button2.setOnClickListener(new View.OnClickListener()
//    {
//      @Override
//      public void onClick(final View v)
//      {
//        Fabric.getInstance().persist(thisActivity, R.id.play_button_1, "count");
//        holder.textView1.setText("Persisting press count");
//      }
//    });
//
//    holder.button3 = (Button)findViewById(R.id.play_button_3);
//    holder.button3.setOnClickListener(new View.OnClickListener()
//    {
//      @Override
//      public void onClick(final View v)
//      {
//        Fabric.getInstance().unpersist(thisActivity, R.id.play_button_1, "count");
//        holder.textView1.setText("Not persisting press count");
//      }
//    });
//
//
//    Fabric.init(getApplicationContext());
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
//    holder.textView2.setText("Button has been pressed " + pressCount + " times");
//  }
}
