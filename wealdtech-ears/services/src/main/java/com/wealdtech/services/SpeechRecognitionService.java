package com.wealdtech.services;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by jgm on 10/05/16.
 */
public class SpeechRecognitionService extends Service implements RecognitionListener
{

  protected SpeechRecognizer speechRecognizer;
  protected Intent speechRecognizerIntent;

  private final Messenger serverMessenger = new Messenger(new IncomingHandler(this));

  public static final int MSG_RECOGNIZER_START_LISTENING = 1;
  public static final int MSG_RECOGNIZER_CANCEL = 2;

  protected boolean listening;

  @Override
  public void onCreate()
  {
    super.onCreate();

    Log.e("weald", "Speech recognition service created");
    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
    speechRecognizer.setRecognitionListener(this);

    speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
    speechRecognizer.startListening(speechRecognizerIntent);

    listening = false;
  }

  protected static class IncomingHandler extends Handler
  {
    private WeakReference<SpeechRecognitionService> target;
    IncomingHandler(final SpeechRecognitionService target)
    {
      this.target = new WeakReference<SpeechRecognitionService>(target);
    }

    @Override
    public void handleMessage(final Message msg)
    {
      final SpeechRecognitionService service = target.get();
      switch (msg.what)
      {
        case MSG_RECOGNIZER_START_LISTENING:
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
          {
            // Turn off beep
          }
          if (!service.listening)
          {
            service.speechRecognizer.startListening(service.speechRecognizerIntent);
            service.listening = true;
          }
          break;
        case MSG_RECOGNIZER_CANCEL:
          service.speechRecognizer.cancel();
          service.listening = false;
          break;
      }
    }
  }

  @Override
  public IBinder onBind(final Intent arg0)
  {
    return serverMessenger.getBinder();
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    if (speechRecognizer != null)
    {
      speechRecognizer.destroy();
    }
  }

  @Override
  public int onStartCommand(final Intent intent, final int flags, final int startId)
  {
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onReadyForSpeech(final Bundle params)
  {

  }

  @Override
  public void onBeginningOfSpeech()
  {

  }

  @Override
  public void onRmsChanged(final float rmsdB)
  {

  }

  @Override
  public void onBufferReceived(final byte[] buffer)
  {

  }

  @Override
  public void onEndOfSpeech()
  {

  }

  @Override
  public void onError(final int error)
  {

  }

  @Override
  public void onResults(final Bundle results)
  {
    Log.e("weald", "Speech recognition service obtained results");
    List<String> possibleResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    for (final String possibleResult : possibleResults)
    {
      Log.e("weald", "Possible result is " + possibleResult);
    }
  }

  @Override
  public void onPartialResults(final Bundle partialResults)
  {

  }

  @Override
  public void onEvent(final int eventType, final Bundle params)
  {

  }
}
