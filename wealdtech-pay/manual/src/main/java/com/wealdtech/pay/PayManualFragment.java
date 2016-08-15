package com.wealdtech.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.wealdtech.android.CountryAdapter;
import com.wealdtech.android.CreditCardView;
import com.wealdtech.android.DateView;
import com.wealdtech.android.NationalityAdapter;
import com.wealdtech.android.pay.manual.R;
import io.card.payment.CardIOActivity;
import org.joda.time.LocalDate;

import java.util.Locale;

import static com.wealdtech.android.DateView.DateChangeViewTrigger.dateChanges;
import static com.wealdtech.android.fabric.Rule.just;
import static com.wealdtech.android.fabric.Rule.when;
import static com.wealdtech.android.fabric.action.TextColorAction.textColor;
import static com.wealdtech.android.fabric.condition.ValidCondition.valid;
import static com.wealdtech.android.fabric.trigger.TextChangeViewTrigger.textChanges;
import static com.wealdtech.android.fabric.validator.EmailValidator.emailValidator;
import static com.wealdtech.android.fabric.validator.PresentValidator.presentValidator;

/**
 * A fragment that provides a button for manually obtaining credit card information.
 * To implement this fully it is required that the calling activity calls this fragment's
 * onActivityResult() method.
 */
public class PayManualFragment extends Fragment
{
  private static final int PAY_CARD_DATA_RESULT = 1432;

  private TextView firstNameLabel;
  private EditText firstName;
  private TextView lastNameLabel;
  private EditText lastName;
  private TextView emailLabel;
  private EditText email;
  private TextView dobLabel;
  private DateView dob;
  private TextView nationalityLabel;
  private AppCompatSpinner nationality;
  private TextView residenceLabel;
  private AppCompatSpinner residence;
  private TextView cardLabel;
  private CreditCardView card;
  private Button submit;

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

    firstNameLabel = (TextView)view.findViewById(R.id.pay_manual_firstname_label);
    firstName = (EditText)view.findViewById(R.id.pay_manual_firstname);
    lastNameLabel = (TextView)view.findViewById(R.id.pay_manual_lastname_label);
    lastName = (EditText)view.findViewById(R.id.pay_manual_lastname);
    emailLabel = (TextView)view.findViewById(R.id.pay_manual_email_label);
    email = (EditText)view.findViewById(R.id.pay_manual_email);
    dobLabel = (TextView)view.findViewById(R.id.pay_manual_dob_label);
    dob = (DateView)view.findViewById(R.id.pay_manual_dob);
    nationalityLabel = (TextView)view.findViewById(R.id.pay_manual_nationality_label);
    nationality = (AppCompatSpinner)view.findViewById(R.id.pay_manual_nationality);
    residenceLabel = (TextView)view.findViewById(R.id.pay_manual_residence_label);
    residence = (AppCompatSpinner)view.findViewById(R.id.pay_manual_residence);
    cardLabel = (TextView)view.findViewById(R.id.pay_manual_card_label);
    card = (CreditCardView)view.findViewById(R.id.pay_manual_card);
    submit = (Button)view.findViewById(R.id.pay_manual_submit);

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

    final NationalityAdapter nationalityAdapter = new NationalityAdapter(getContext());
    nationality.setAdapter(nationalityAdapter);
    nationality.setSelection(nationalityAdapter.positionForCode(Locale.getDefault().getCountry()));
    nationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l)
      {
        //        nationality = nationalityAdapter.getItem(i).getCode();
      }

      @Override
      public void onNothingSelected(final AdapterView<?> adapterView){}
    });


    final CountryAdapter residenceAdapter = new CountryAdapter(getContext());
    residence.setAdapter(residenceAdapter);
    residence.setSelection(residenceAdapter.positionForCode(Locale.getDefault().getCountry()));
    residence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override
      public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l)
      {
        //        residence = residenceAdapter.getItem(i).getCode();
      }

      @Override
      public void onNothingSelected(final AdapterView<?> adapterView){}
    });

    setupValidationLogic();
    return view;
  }

  private void setupValidationLogic()
  {
    final int validColor = ContextCompat.getColor(getContext(), R.color.validInput);
    final int invalidColor = ContextCompat.getColor(getContext(), R.color.invalidInput);

    // Set initial colours
    just(textColor(firstNameLabel, invalidColor));
    just(textColor(lastNameLabel, invalidColor));
    just(textColor(emailLabel, invalidColor));
    just(textColor(dobLabel, invalidColor));
    just(textColor(nationalityLabel, invalidColor));
    just(textColor(residenceLabel, invalidColor));
    just(textColor(cardLabel, invalidColor));

    // Set label validation colour
    when(textChanges(firstName)).and(valid(firstName, presentValidator()))
                                         .then(textColor(firstNameLabel, validColor))
                                         .otherwise(textColor(firstNameLabel, invalidColor));

    when(textChanges(lastName)).and(valid(lastName, presentValidator()))
                                        .then(textColor(lastNameLabel, validColor))
                                        .otherwise(textColor(lastNameLabel, invalidColor));

    when(textChanges(email)).and(valid(email, emailValidator()))
                                     .then(textColor(emailLabel, validColor))
                                     .otherwise(textColor(emailLabel, invalidColor));

    when(dateChanges(dob)).and(valid(dob, presentValidator()))
                                     .then(textColor(dobLabel, validColor))
                                     .otherwise(textColor(dobLabel, invalidColor));

//    when(creditCardChanges(card)).and(valid(card, creditCardValidator()))
//                                          .then(textColor(cardLabel, validColor))
//                                          .otherwise(textColor(cardLabel, invalidColor));
  }

  /**
   * Programmatic instantiation of the fragment.
   *
   * @return
   */
  public static PayManualFragment newInstance()
  {
    return new PayManualFragment();
  }

  public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
  {
//    boolean success = false;
//    if (requestCode == PAY_CARD_DATA_RESULT)
//    {
//      if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
//      {
//        final CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
//        card.setCardNumber(scanResult.cardNumber, true);
//        //        cardholderName = scanResult.cardholderName;
//        card.setExpDate(scanResult.expiryMonth + "/" + scanResult.expiryYear, true);
//        card.setSecurityCode(scanResult.cvv, true);
//        success = true;
//      }
//    }
//    if (!success)
//    {
//      // TODO Wipe the last information
//    }
  }

  public String getFirstName()
  {
    return getCleanText(firstName);
  }

  public void setFirstName(final String value)
  {
    firstName.setText(value);
  }

  public String getLastName()
  {
    return getCleanText(lastName);
  }

  public void setLastName(final String value)
  {
    lastName.setText(value);
  }

  public String getEmail()
  {
    return getCleanText(email);
  }

  public void setEmail(final String value)
  {
    email.setText(value);
  }

  public LocalDate getDob()
  {
    return dob.getDate();
  }

  public void setDob(final LocalDate date)
  {
    dob.setDate(date);
  }

  public String getNationality()
  {
    return ((NationalityAdapter.Nationality)nationality.getSelectedItem()).getCode();
  }

  public void setNationality(final String code)
  {
    final int pos = ((NationalityAdapter)nationality.getAdapter()).positionForCode(code);
    if (pos == -1)
    {
      Log.w("paymanual", "Attempt to set unknown nationality " + code);
    }
    else
    {
      nationality.setSelection(pos);
    }
  }

  public String getResidence()
  {
    return ((CountryAdapter.Country)residence.getSelectedItem()).getCode();
  }

  public void setResidence(final String code)
  {
    final int pos = ((CountryAdapter)residence.getAdapter()).positionForCode(code);
    if (pos == -1)
    {
      Log.w("paymanual", "Attempt to set unknown country of residence " + code);
    }
    else
    {
      nationality.setSelection(pos);
    }
  }

//  public String getCardNumber()
//  {
//    return card.getCreditCard().getCardNumber();
//  }
//
//  public void setCardNumber(final String cardNumber)
//  {
//    card.setCardNumber(cardNumber, true);
//  }
//
//  public String getCvv()
//  {
//    return card.getCreditCard().getSecurityCode();
//  }
//
//  public YearMonth getExpiry()
//  {
//    return new YearMonth(card.getCreditCard().getExpYear() + 2000, card.getCreditCard().getExpMonth());
//  }
//
//  public void setExpiry(final YearMonth expiry)
//  {
//    card.setExpDate(expiry.getMonthOfYear() + "/" + (expiry.getYear() - 2000), true);
//  }

  /**
   * Obtain a sane version of a text view
   *
   * @param view the view
   *
   * @return a clean version of the contents of the text view; empty string will be {@code null} rather than ""
   */
  @Nullable
  public static String getCleanText(final TextView view)
  {
    return getCleanText(view.getText().toString());
  }

  /**
   * Obtain a sane string given an input string
   *
   * @param val the input string
   *
   * @return a clean version of the contents of the string; empty string will be {@code null} rather than ""
   */
  @Nullable
  public static String getCleanText(String val)
  {
    if (val != null)
    {
      val = val.trim();
    }
    if (val == null || val.equals(""))
    {
      return null;
    }
    return val;
  }
}
