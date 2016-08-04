package com.wealdtech.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.wealdtech.android.pay.manual.R;
import io.card.payment.CardIOActivity;

/**
 * A fragment that provides a button for manually obtaining credit card information
 */
public class PayManualFragment extends Fragment implements View.OnClickListener
{
  public static final int PAY_CARD_DATA_RESULT = 1432;

  private Button button;

  public PayManualFragment() {}

  @Override
  public void onCreate(@Nullable final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.pay_manual_fragment, container, false);
    button = (Button)view.findViewById(R.id.pay_manual_button);
    button.setOnClickListener(this);
    return view;
  }

  public static PayManualFragment newInstance()
  {
    return new PayManualFragment();
  }

  @Override
  public void onClick(final View view)
  {
    Intent scanIntent = new Intent(getContext(), CardIOActivity.class);

    // customize these values to suit your needs.
    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

    // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
    startActivityForResult(scanIntent, PAY_CARD_DATA_RESULT);
  }
}
