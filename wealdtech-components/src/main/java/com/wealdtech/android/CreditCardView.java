package com.wealdtech.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wealdtech.CreditCard;
import com.wealdtech.android.components.R;
import com.wealdtech.android.fabric.Rule;
import com.wealdtech.android.fabric.action.Action;
import com.wealdtech.android.fabric.validator.TextValidator;

import java.util.Locale;

import static com.wealdtech.android.CreditCardView.CreditCardNumberValidator.creditCardNumberValidator;
import static com.wealdtech.android.CreditCardView.CvcValidator.cvcValidator;
import static com.wealdtech.android.CreditCardView.ExpiryDateValidator.expiryDateValidator;
import static com.wealdtech.android.fabric.Rule.just;
import static com.wealdtech.android.fabric.Rule.when;
import static com.wealdtech.android.fabric.action.TextColorAction.textColor;
import static com.wealdtech.android.fabric.condition.ValidCondition.valid;
import static com.wealdtech.android.fabric.trigger.TextChangeViewTrigger.textChanges;
import static com.wealdtech.android.fabric.trigger.Trigger.happens;

/**
 * A two-line credit card entry view, obtaining number, expiry date and CVV
 */
public class CreditCardView extends RelativeLayout
{

  private TextView numberLabel;
  private EditText number;
  private TextView expiryLabel;
  private EditText expiry;
  private TextView cscLabel;
  private EditText csc;
  private ImageView logo;
  private ImageView cscImage;

  private CreditCard.Brand brand;

  public CreditCardView(final Context context)
  {
    super(context);
    initializeView(context);
  }

  public CreditCardView(final Context context, final AttributeSet attrs)
  {
    super(context, attrs);
    initializeView(context);
  }

  public CreditCardView(final Context context, final AttributeSet attrs, final int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    initializeView(context);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CreditCardView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
  {
    super(context, attrs, defStyleAttr, defStyleRes);
    initializeView(context);
  }

  /**
   * Inflates the views in the layout.
   *
   * @param context the current context for the view.
   */
  private void initializeView(Context context)
  {
    final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View view = inflater.inflate(R.layout.credit_card_view, this);
    numberLabel = (TextView)view.findViewById(R.id.credit_card_number_label);
    number = (EditText)view.findViewById(R.id.credit_card_number);
    expiryLabel = (TextView)view.findViewById(R.id.credit_card_expiry_label);
    expiry = (EditText)view.findViewById(R.id.credit_card_expiry);
    cscLabel = (TextView)view.findViewById(R.id.credit_card_csc_label);
    csc = (EditText)view.findViewById(R.id.credit_card_csc);
    logo = (ImageView)view.findViewById(R.id.credit_card_logo);
    cscImage = (ImageView)view.findViewById(R.id.credit_card_csc_image);

    final int validColor = ContextCompat.getColor(getContext(), R.color.validInput);
    final int invalidColor = ContextCompat.getColor(getContext(), R.color.invalidInput);

    // Initial settings
    just(textColor(numberLabel, invalidColor));
    just(textColor(expiryLabel, invalidColor));
    just(textColor(cscLabel, invalidColor));

    // Update validity markers
    when(happens(textChanges(number))).and(valid(number, creditCardNumberValidator()))
                                      .then(textColor(numberLabel, validColor))
                                      .otherwise(textColor(numberLabel, invalidColor));
    when(happens(textChanges(expiry))).and(valid(expiry, expiryDateValidator()))
                                      .then(textColor(expiryLabel, validColor))
                                      .otherwise(textColor(expiryLabel, invalidColor));
    when(happens(textChanges(csc))).and(valid(csc, cvcValidator()))
                                   .then(textColor(cscLabel, validColor))
                                   .otherwise(textColor(expiryLabel, invalidColor));

    // Keep elements up-to-date with the metadata we work out from the credit card number
    final String packageName = context.getPackageName();
    when(happens(textChanges(number))).then(new Action()
    {
      @Override
      public void act(final Rule rule)
      {
        final CreditCard.Brand brand = CreditCard.Brand.fromCardNumber(number.getText().toString());
        if (brand == null && CreditCardView.this.brand != null || brand != null && !brand.equals(CreditCardView.this.brand))
        {
          // We have an update
          CreditCardView.this.brand = brand;
          if (brand == null)
          {
            cscLabel.setText("CSC");
            logo.setImageResource(R.drawable.creditcard_generic);
          }
          else
          {
            cscLabel.setText(brand.cscName);
            final String logoName = "logo_" + brand.name().toLowerCase(Locale.ENGLISH);
            final int logoResId = getResources().getIdentifier(logoName, "drawable", packageName);
            logo.setImageResource(logoResId == 0 ? R.drawable.creditcard_generic : logoResId);

            final String cscName = "creditcard_" + brand.name().toLowerCase(Locale.ENGLISH) + "_csc";
            final int cscResId = getResources().getIdentifier(cscName, "drawable", packageName);
            cscImage.setImageResource(cscResId == 0 ? R.drawable.creditcard_generic_csc : cscResId);
          }
        }
      }
    });
  }

  public static class CreditCardNumberValidator extends TextValidator
  {
    private static CreditCardNumberValidator instance;

    private CreditCardNumberValidator()
    {
      super();
    }

    @Override
    public boolean validate(final View view)
    {
      final String val = ((TextView)view).getText().toString().trim();
      return val.length() > 0;
    }

    /**
     * A validator which validates a credit card
     */
    public static CreditCardNumberValidator creditCardNumberValidator()
    {
      if (instance == null)
      {
        synchronized (CreditCardNumberValidator.class)
        {
          // Double check
          if (instance == null)
          {
            instance = new CreditCardNumberValidator();
          }
        }
      }
      return instance;
    }
  }

  public static class ExpiryDateValidator extends TextValidator
  {
    private static ExpiryDateValidator instance;

    private ExpiryDateValidator()
    {
      super();
    }

    @Override
    public boolean validate(final View view)
    {
      final String val = ((TextView)view).getText().toString().trim();
      return val.length() > 0;
    }

    /**
     * A validator which validates an expiry date for a credit card
     */
    public static ExpiryDateValidator expiryDateValidator()
    {
      if (instance == null)
      {
        synchronized (ExpiryDateValidator.class)
        {
          // Double check
          if (instance == null)
          {
            instance = new ExpiryDateValidator();
          }
        }
      }
      return instance;
    }
  }

  public static class CvcValidator extends TextValidator
  {
    private static CvcValidator instance;

    private CvcValidator()
    {
      super();
    }

    @Override
    public boolean validate(final View view)
    {
      final String val = ((TextView)view).getText().toString().trim();
      return val.length() > 0;
    }

    /**
     * A validator which validates a CVC for a credit card
     */
    public static CvcValidator cvcValidator()
    {
      if (instance == null)
      {
        synchronized (CvcValidator.class)
        {
          // Double check
          if (instance == null)
          {
            instance = new CvcValidator();
          }
        }
      }
      return instance;
    }
  }
}
