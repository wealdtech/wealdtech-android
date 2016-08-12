package com.wealdtech.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import com.wealdtech.android.CountryAdapter;
import com.wealdtech.android.NationalityAdapter;
import com.wealdtech.android.pay.manual.R;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.util.Locale;

/**
 * A fragment that provides a button for manually obtaining credit card information.
 * To implement this fully it is required that the calling activity calls this fragment's
 * onActivityResult() method.
 */
public class PayManualFragment extends Fragment
{
  private static final int PAY_CARD_DATA_RESULT = 1432;

  private String cardNumber;
  private String cardholderName;
  private Integer expiryMonth;
  private Integer expiryYear;
  private String cvv;

  public PayManualFragment(){}

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
    final Button scanButton = (Button)view.findViewById(R.id.pay_manual_scan_button);
    scanButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(final View view)
      {
        Intent scanIntent = new Intent(getContext(), CardIOActivity.class);

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);

        startActivityForResult(scanIntent, PAY_CARD_DATA_RESULT);
      }
    });

    final Spinner nationalitySpinner = (Spinner)view.findViewById(R.id.pay_manual_nationality);
    final NationalityAdapter nationalityAdapter = new NationalityAdapter(getContext());
    nationalitySpinner.setAdapter(nationalityAdapter);
    nationalitySpinner.setSelection(nationalityAdapter.positionForCode(Locale.getDefault().getCountry()));
    nationalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l)
      {
      }

      @Override
      public void onNothingSelected(final AdapterView<?> adapterView){}
    });


    final Spinner residenceSpinner = (Spinner)view.findViewById(R.id.pay_manual_residence);
    final CountryAdapter residenceAdapter = new CountryAdapter(getContext());
    residenceSpinner.setAdapter(residenceAdapter);
    residenceSpinner.setSelection(residenceAdapter.positionForCode(Locale.getDefault().getCountry()));
    residenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l)
      {
      }

      @Override
      public void onNothingSelected(final AdapterView<?> adapterView){}
    });

    //    typeButton = (Button)view.findViewById(R.id.pay_manual_type_button);
//    typeButton.setOnClickListener(new View.OnClickListener()
//    {
//      @Override
//      public void onClick(final View view)
//      {
//        Log.e("PayManual", "Not implemented");
//      }
//    });

    return view;
  }

  public static PayManualFragment newInstance()
  {
    return new PayManualFragment();
  }

  public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
  {
    boolean success = false;
    if (requestCode == PAY_CARD_DATA_RESULT)
    {
      if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
      {
        final CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
        cardNumber = scanResult.cardNumber;
        cardholderName = scanResult.cardholderName;
        expiryMonth = scanResult.expiryMonth;
        expiryYear = scanResult.expiryYear;
        cvv = scanResult.cvv;
        success = true;
      }
    }
    if (!success)
    {
      // Wipe the last information
      cardNumber = cardholderName = cvv = null;
      expiryMonth = expiryYear = null;
    }
  }

  public String getCardNumber()
  {
    return cardNumber;
  }

  public String getCardholderName()
  {
    return cardholderName;
  }

  public String getCvv()
  {
    return cvv;
  }

  public Integer getExpiryMonth()
  {
    return expiryMonth;
  }

  public Integer getExpiryYear()
  {
    return expiryYear;
  }
}
