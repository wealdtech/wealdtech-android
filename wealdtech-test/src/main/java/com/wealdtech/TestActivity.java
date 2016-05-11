package com.wealdtech;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.*;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.wealdtech.services.EarsServicePocketSphinxImpl;
import com.wealdtech.services.SpeechRecognitionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 */
public class TestActivity extends Activity
{
  private static final String TAG = "WealdTest";

  private ListView wordsList;


  private Messenger serviceMessenger;
  private int bindFlag;

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.voice_recog);

//    Button speakButton = (Button)findViewById(R.id.speakButton);

    wordsList = (ListView)findViewById(R.id.list);

    Log.e(TAG, "onCreate()");

    final PackageManager pm = getPackageManager();
    List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
    if (activities.isEmpty())
    {
      Log.e(TAG, "No speech recognizer present");
    }

    final Intent service = new Intent(this, SpeechRecognitionService.class);
    bindFlag = Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ? 0 : Context.BIND_ABOVE_CLIENT;
    startService(service);
  }

  @Override
  protected void onStart()
  {
    super.onStart();

//    final Intent intent = new Intent(this, ContinuousSpeechRecognitionService.class);
    final Intent intent = new Intent(this, EarsServicePocketSphinxImpl.class);
    Log.e(TAG, "onStart(): about to start service");
    startService(intent);
    Log.e(TAG, "onStart(): started service");
//    bindService(new Intent(this, SpeechRecognitionService.class), serviceConnection, bindFlag);
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    if (serviceMessenger != null)
    {
      unbindService(serviceConnection);
      serviceMessenger = null;
    }
  }

  private final ServiceConnection serviceConnection = new ServiceConnection()
  {
    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service)
    {
      serviceMessenger = new Messenger(service);
      final Message msg = new Message();
      msg.what = SpeechRecognitionService.MSG_RECOGNIZER_START_LISTENING;
      try
      {
        serviceMessenger.send(msg);
      }
      catch (final RemoteException re)
      {
        re.printStackTrace();
      }
    }

    @Override
    public void onServiceDisconnected(final ComponentName name)
    {
      serviceMessenger = null;
    }
  };

  public void speakButtonClicked(final View v)
  {
    startVoiceRecognitionActivity();
  }

  private void startVoiceRecognitionActivity()
  {
    final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
    startActivityForResult(intent, 12345);
  }


//  @Override
//  protected void onResume()
//  {
//    super.onResume();
//    startVoiceRecognitionActivity();
////    recognize(RecognizerIntentFactory.getSimpleRecognizerIntent("Speak friend, and enter"));
//  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    Log.i(TAG, "in onActivityResult()");
    if (requestCode == 12345 && resultCode == RESULT_OK)
    {
      // Populate the wordsList with the String values the recognition engine thought it heard
      ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
    }
  }
    //  @Override
//  protected void speechNotAvailable()
//  {
//    Log.i(TAG, "Speech not available");
//  }
//
//  @Override
//  protected void directSpeechNotAvailable()
//  {
//    Log.i(TAG, "Direct speech not available");
//  }
//
//  @Override
//  protected void languageCheckResult(final String languageToUse)
//  {
//    Log.i(TAG, "Language check result is " + languageToUse);
//  }
//
//  @Override
//  protected void receiveWhatWasHeard(final List<String> heard, final float[] confidenceScores)
//  {
//    for (final String line : heard)
//    {
//      Log.i(TAG, "Heard " + line);
//    }
//
//  }
//
//  @Override
//  protected void recognitionFailure(final int errorCode)
//  {
//    Log.i(TAG, "Recognition failure error code " + errorCode);
//  }
}
