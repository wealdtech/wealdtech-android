package com.wealdtech.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jgm on 10/05/16.
 */
public class ContinuousSpeechRecognitionService extends Service implements RecognitionListener
{

  @Nullable
  @Override
  public IBinder onBind(final Intent intent)
  {
    Log.e("weald", "onBind()");
    return null;
  }

  private SpeechRecognizer speechRecognizer;

  @Override
  public void onCreate()
  {
    super.onCreate();

    Log.e("weald", "onCreate()");

    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
    speechRecognizer.setRecognitionListener(this);

    final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
    speechRecognizer.startListening(speechRecognizerIntent);
  }

  @Override
  public void onDestroy()
  {
    Log.e("weald", "onDestroy");
    speechRecognizer.destroy();
    speechRecognizer = null;
    super.onDestroy();
  }

  @Override
  public int onStartCommand(final Intent intent, final int flags, final int startId)
  {
    Log.e("weald", "onStartCommand");
    return START_STICKY;
  }

  @Override
  public void onReadyForSpeech(final Bundle params)
  {
    Log.e("weald", "onReadyForSpeech");
  }

  @Override
  public void onBeginningOfSpeech()
  {
    Log.e("weald", "onBeginningOfSpeech");
  }

  @Override
  public void onRmsChanged(final float rmsdB)
  {
    Log.e("weald", "onRmsChanged");
  }

  @Override
  public void onBufferReceived(final byte[] buffer)
  {
    Log.e("weald", "onBufferReceived");
  }

  @Override
  public void onEndOfSpeech()
  {
    Log.e("weald", "onEndOfSpeech");
  }

  @Override
  public void onError(final int error)
  {
    Log.e("weald", "onError: " + error);
  }

  @Override
  public void onResults(final Bundle results)
  {
    Log.e("weald", "onResults");
  }

  @Override
  public void onPartialResults(final Bundle partialResults)
  {
    Log.e("weald", "onPartialResults");
  }

  @Override
  public void onEvent(final int eventType, final Bundle params)
  {
    Log.e("weald", "onEvent");
  }
}
