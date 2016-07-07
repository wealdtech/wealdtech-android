package com.wealdtech.android.fabric;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wealdtech.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import static com.wealdtech.Preconditions.checkState;

/**
 * A fragment with additional features:
 * <ul>
 *   <li>Progress dialog when loading data</li>
 * </ul>
 */
public abstract class WealdFragment extends Fragment
{
  private static final Logger LOG = LoggerFactory.getLogger(WealdFragment.class);

  private LinearLayout progressContainer;
  private String pendingText = null;
  private TextView progressText;
  private ViewGroup contentContainer;
  private View contentView;
  private boolean contentShown = true;

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
  {
    final ViewGroup view = (ViewGroup)inflater.inflate(R.layout.weald_fragment, container, false);
    progressContainer = findById(view, R.id.progress_container);
    progressText = findById(progressContainer, R.id.progress_text);
    if (pendingText != null)
    {
      progressText.setText(pendingText);
      pendingText = null;
    }
    contentContainer = findById(view, R.id.content_container);

    // Display the correct container
    progressContainer.setVisibility(contentShown ? View.GONE : View.VISIBLE);
    contentContainer.setVisibility(contentShown ? View.VISIBLE : View.GONE);
    return view;
  }

  @Override
  public void onViewCreated(final View view, final Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
  }

//  @Override
//  public void onAttach(final Activity activity)
//  {
//    super.onAttach(activity);
//    if ((activity instanceof CognisFragmentListener))
//    {
//      fragmentListener = (CognisFragmentListener)getActivity();
//    }
//    fragmentListener.onFragmentAttached(this);
//  }
//
//  @Override
//  public void onDetach()
//  {
//    super.onDetach();
//    fragmentListener.onFragmentDetached(this);
//    fragmentListener = DUMMY_LISTENER;
//  }

  @Override
  public void onDestroyView()
  {
    progressContainer = null;
    progressText = null;
    contentContainer = null;
    contentShown = false;
    super.onDestroyView();
  }

  @SuppressWarnings("unchecked")
  public <T> T findById(final View view, final int id)
  {
    final T result = (T)view.findViewById(id);
    if (result == null)
    {
      throw new ServerError("Failed to obtain view");
    }
    return result;
  }

  /**
   * Obtain a string value from (preferentially) our saved instance state or from our fragment arguments
   */
  @Nullable
  public String obtainString(final String key, @Nullable final Bundle savedInstanceState, @Nullable final Bundle arguments)
  {
    String res = null;
    if (savedInstanceState != null)
    {
      res = savedInstanceState.getString(key);
    }
    if (res == null && arguments != null)
    {
      res = arguments.getString(key);
    }
    return res;
  }

  /**
   * Obtain an integer value from (preferentially) our saved instance state or from our fragment arguments
   */
  public int obtainInt(final String key,
                         @Nullable final Bundle savedInstanceState,
                         @Nullable final Bundle arguments,
                         final int defaultVal)
  {
    int res = defaultVal;
    if (savedInstanceState != null)
    {
      res = savedInstanceState.getInt(key, defaultVal);
    }
    if (res == defaultVal && arguments != null)
    {
      res = arguments.getInt(key, defaultVal);
    }
    return res;
  }

  /**
   * Obtain a long value from (preferentially) our saved instance state or from our fragment arguments
   */
  public long obtainLong(final String key,
                         @Nullable final Bundle savedInstanceState,
                         @Nullable final Bundle arguments,
                         final long defaultVal)
  {
    long res = defaultVal;
    if (savedInstanceState != null)
    {
      res = savedInstanceState.getLong(key, defaultVal);
    }
    if (res == defaultVal && arguments != null)
    {
      res = arguments.getLong(key, defaultVal);
    }
    return res;
  }

  // Methods to handle setting content view for the fragment, and displaying the wait state

  @SuppressWarnings("unchecked")
  public <T extends View> T setContentView(final int layoutResId)
  {
    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    T contentView = (T)layoutInflater.inflate(layoutResId, null);
    setContentView(contentView);
    return contentView;
  }

  public void setContentView(final View view)
  {
    checkState(view != null, "Cannot set content to NULL");
    checkState(view instanceof ViewGroup, "Cannot use non-container for content");

    if (contentView == null)
    {
      contentContainer.addView(view);
    }
    else
    {
      final int index = contentContainer.indexOfChild(contentView);
      // replace content view
      contentContainer.removeView(contentView);
      contentContainer.addView(view, index);
    }
    contentView = view;
  }

  public boolean isContentShown(){ return contentShown; }

  public void setContentShown(final boolean shown)
  {
    setContentShown(shown, true);
  }

  private void setContentShown(final boolean shown, final boolean animate)
  {
    if (contentShown == shown)
    {
      return;
    }
    contentShown = shown;

    // Because setContentShown() is commonly called in loaders, which are commonly set up in onCreate(), there
    // is no guarantee that our view is set up yet.  Our onCreateView() handles this situation by setting the
    // correct views depending on the value of contentShown
    if (progressContainer != null && contentContainer != null)
    {
      if (shown)
      {
        if (animate)
        {
          progressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
          contentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }
        else
        {
          progressContainer.clearAnimation();
          contentContainer.clearAnimation();
        }
        progressContainer.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
      }
      else
      {
        if (animate)
        {
          progressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
          contentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        }
        else
        {
          progressContainer.clearAnimation();
          contentContainer.clearAnimation();
        }
        progressContainer.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
      }
    }
  }

  public void setWaitingText(final String text)
  {
    // Because setWaitingText() is commonly called in loaders, which are commonly set up in onCreate(), there
    // is no guarantee that our view is set up yet.  Our onCreateView() handles this situation by setting the
    // text if present
    if (progressText == null)
    {
      pendingText = text;
    }
    else
    {
      progressText.setText(text);
    }
  }

  public void setWaitingText(final int resId)
  {
    setWaitingText(getResources().getString(resId));
  }
}
