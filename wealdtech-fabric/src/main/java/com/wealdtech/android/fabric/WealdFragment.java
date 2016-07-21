package com.wealdtech.android.fabric;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.wealdtech.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wealdtech.Preconditions.checkState;

/**
 * A fragment with additional features:
 * <ul>
 *   <li>Separate content views for progress, display, error and no data (empty)</li>
 * </ul>
 */
public abstract class WealdFragment extends Fragment
{
  private static final Logger LOG = LoggerFactory.getLogger(WealdFragment.class);

  private static final int PROGRESS = 1;
  private static final int CONTENT = 2;
  private static final int EMPTY = 3;
  private static final int ERROR = 4;

  private ViewGroup progressContainer;
  private ViewGroup contentContainer;
  private ViewGroup emptyContainer;
  private ViewGroup errorContainer;

  private View progressView;
  private View contentView;
  private View errorView;
  private View emptyView;

  // Direct access to the textviews in our default layouts
  private TextView progressText;
  private TextView errorText;
  private TextView emptyText;

  // Temporary stores in case the user has attempted to set up texts before the views have been inflated
  private View progressViewTmp = null;
  private View contentViewTmp = null;
  private View errorViewTmp = null;
  private View emptyViewTmp = null;
  private String progressTextTmp = null;
  private String errorTextTmp = null;
  private String emptyTextTmp = null;
  
  // The current shown content
  private int shown;

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
  {
    final ViewGroup view = (ViewGroup)inflater.inflate(R.layout.weald_fragment, container, false);

    // Obtain our top-level containers
    progressContainer = findById(view, R.id.progress_container);
    contentContainer = findById(view, R.id.content_container);
    emptyContainer = findById(view, R.id.empty_container);
    errorContainer = findById(view, R.id.error_container);

    // Set defaults for our status pages
    progressText = findById(progressContainer, R.id.progress_text);
    errorText = findById(errorContainer, R.id.error_text);
    emptyText = findById(emptyContainer, R.id.empty_text);

    // Override the default setup if
    if (progressTextTmp != null)
    {
      progressText.setText(progressTextTmp);
      progressTextTmp = null;
    }
    else
    {
      progressText.setText(getResources().getString(R.string.weald_fragment_default_progress_text));
    }
    if (errorTextTmp != null)
    {
      errorText.setText(errorTextTmp);
      errorTextTmp = null;
    }
    else
    {
      errorText.setText(getResources().getString(R.string.weald_fragment_default_error_text));
    }
    if (emptyTextTmp != null)
    {
      emptyText.setText(emptyTextTmp);
      emptyTextTmp = null;
    }
    else
    {
      emptyText.setText(getResources().getString(R.string.weald_fragment_default_empty_text));
    }


    if (progressViewTmp != null)
    {
      setProgressView(progressViewTmp);
      progressViewTmp = null;
    }
    if (contentViewTmp != null)
    {
      setContentView(contentViewTmp);
      contentViewTmp = null;
    }
    if (emptyViewTmp != null)
    {
      setEmptyView(emptyViewTmp);
      emptyViewTmp = null;
    }
    if (errorViewTmp != null)
    {
      setErrorView(errorViewTmp);
      errorViewTmp = null;
    }

    // Display the correct container
    progressContainer.setVisibility(shown == PROGRESS ? VISIBLE : GONE);
    contentContainer.setVisibility(shown == CONTENT ? VISIBLE : GONE);
    emptyContainer.setVisibility(shown == EMPTY ? VISIBLE : GONE);
    errorContainer.setVisibility(shown == ERROR ? VISIBLE : GONE);
    return view;
  }

  @Override
  public void onDestroyView()
  {
    progressContainer = null;
    progressText = null;
    contentContainer = null;
    emptyContainer = null;
    emptyText = null;
    errorContainer = null;
    errorText = null;
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

  @SuppressWarnings("unchecked")
  public <T extends View> T setProgressView(final int layoutResId)
  {
    final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    final T progressView = (T)layoutInflater.inflate(layoutResId, null);
    setProgressView(progressView);
    return progressView;
  }

  public void setProgressView(final View view)
  {
    checkState(view != null, "Cannot set progress to NULL");
    checkState(view instanceof ViewGroup, "Cannot use non-container for progress");

    if (progressContainer == null)
    {
      progressViewTmp = view;
    }
    else
    {
      if (progressView == null)
      {
        progressContainer.addView(view);
      }
      else
      {
        final int index = progressContainer.indexOfChild(progressView);
        // replace progress view
        progressContainer.removeView(progressView);
        progressContainer.addView(view, index);
      }
      progressView = view;
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends View> T setContentView(final int layoutResId)
  {
    final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    final T contentView = (T)layoutInflater.inflate(layoutResId, null);
    setContentView(contentView);
    return contentView;
  }

  public void setContentView(final View view)
  {
    checkState(view != null, "Cannot set content to NULL");
    checkState(view instanceof ViewGroup, "Cannot use non-container for content");

    if (contentContainer == null)
    {
      contentViewTmp = view;
    }
    else
    {
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
  }

  @SuppressWarnings("unchecked")
  public <T extends View> T setErrorView(final int layoutResId)
  {
    final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    final T errorView = (T)layoutInflater.inflate(layoutResId, null);
    setErrorView(errorView);
    return errorView;
  }

  public void setErrorView(final View view)
  {
    checkState(view != null, "Cannot set error content to NULL");
    checkState(view instanceof ViewGroup, "Cannot use non-container for error content");

    if (errorContainer == null)
    {
      errorViewTmp = view;
    }
    else
    {
      if (errorView == null)
      {
        errorContainer.addView(view);
      }
      else
      {
        final int index = errorContainer.indexOfChild(errorView);
        // replace existing view
        errorContainer.removeView(errorView);
        errorContainer.addView(view, index);
      }
      errorView = view;
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends View> T setEmptyView(final int layoutResId)
  {
    final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
    final T emptyView = (T)layoutInflater.inflate(layoutResId, null);
    setEmptyView(emptyView);
    return emptyView;
  }

  public void setEmptyView(final View view)
  {
    checkState(view != null, "Cannot set empty content to NULL");
    checkState(view instanceof ViewGroup, "Cannot use non-container for empty content");

    if (emptyContainer == null)
    {
      emptyViewTmp = view;
    }
    else
    {
      if (emptyView == null)
      {
        emptyContainer.addView(view);
      }
      else
      {
        final int index = emptyContainer.indexOfChild(emptyView);
        emptyContainer.removeView(emptyView);
        emptyContainer.addView(view, index);
      }
      emptyView = view;
    }
  }

  public boolean isContentShown(){ return shown == CONTENT; }

  public boolean isProgressShown(){ return shown == PROGRESS; }

  public boolean isEmptyShown(){ return shown == EMPTY; }

  public boolean isErrorShown(){ return shown == ERROR; }

  public void showContent() { show(CONTENT, true); }

  public void showProgress() { show(PROGRESS, true); }

  public void showEmpty() { show(EMPTY, true); }

  public void showError() { show(ERROR, true); }

  private void show(final int toShow, final boolean animate)
  {
    if (toShow == shown)
    {
      // Already showing this
      return;
    }

    if ((toShow == PROGRESS && progressContainer == null) || (toShow == CONTENT && contentContainer == null) ||
        (toShow == ERROR && errorContainer == null) || (toShow == EMPTY && emptyContainer == null))
    {
      // We are here before onCreateView() has been called.  This is fine; remember the text and let onCreateView() handle it
      shown = toShow;
      return;
    }

    // First off get rid of whatever we need to get rid of
    switch(shown)
    {
      case PROGRESS:
        if (animate)
        {
          progressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        }
        else
        {
          progressContainer.clearAnimation();
        }
        progressContainer.setVisibility(GONE);
        break;
      case CONTENT:
        if (animate)
        {
          contentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        }
        else
        {
          contentContainer.clearAnimation();
        }
        contentContainer.setVisibility(GONE);
        break;
      case EMPTY:
        if (animate)
        {
          emptyContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        }
        else
        {
          emptyContainer.clearAnimation();
        }
        emptyContainer.setVisibility(GONE);
        break;
      case ERROR:
        if (animate)
        {
          errorContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        }
        else
        {
          errorContainer.clearAnimation();
        }
        errorContainer.setVisibility(GONE);
        break;
    }

    // Now show whatever we need to show
    switch(toShow)
    {
      case PROGRESS:
        if (animate)
        {
          progressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }
        else
        {
          progressContainer.clearAnimation();
        }
        progressContainer.setVisibility(VISIBLE);
        break;
      case CONTENT:
        if (animate)
        {
          contentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }
        else
        {
          contentContainer.clearAnimation();
        }
        contentContainer.setVisibility(VISIBLE);
        break;
      case EMPTY:
        if (animate)
        {
          emptyContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }
        else
        {
          emptyContainer.clearAnimation();
        }
        emptyContainer.setVisibility(VISIBLE);
        break;
      case ERROR:
        if (animate)
        {
          errorContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        }
        else
        {
          errorContainer.clearAnimation();
        }
        errorContainer.setVisibility(VISIBLE);
        break;
    }
    shown = toShow;
  }

  public void setProgressText(final int resId)
  {
    setProgressText(getResources().getString(resId));
  }

  public void setProgressText(final String text)
  {
    if (progressText == null)
    {
      // We are here before onCreateView() has been called.  This is fine; remember the text and let onCreateView() handle it
      progressTextTmp = text;
    }
    else
    {
      progressText.setText(text);
    }
  }

  public void setErrorText(final String text)
  {
    if (errorText == null)
    {
      // We are here before onCreateView() has been called.  This is fine; remember the text and let onCreateView() handle it
      errorTextTmp = text;
    }
    else
    {
      errorText.setText(text);
    }
  }

  public void setErrorText(final int resId)
  {
    setErrorText(getResources().getString(resId));
  }

  public void setEmptyText(final String text)
  {
    if (emptyText == null)
    {
      // We are here before onCreateView() has been called.  This is fine; remember the text and let onCreateView() handle it
      emptyTextTmp = text;
    }
    else
    {
      emptyText.setText(text);
    }
  }

  public void setEmptyText(final int resId)
  {
    setEmptyText(getResources().getString(resId));
  }
}
