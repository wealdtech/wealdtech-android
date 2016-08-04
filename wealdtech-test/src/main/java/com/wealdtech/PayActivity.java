package com.wealdtech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.*;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.common.base.Objects;
import com.wealdtech.android.test.R;
import com.wealdtech.pay.PayManualFragment;

/**
 * A sample activity using wealdtech-pay
 */
public class PayActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
                                                                  GoogleApiClient.OnConnectionFailedListener
{
  private static final String TAG = "Pay";

  // You will need to use your live API key even while testing
  public static final String PUBLISHABLE_KEY = "pk_live_FZPD37WGvWhgIP1iycgqCLa2";

  // Unique identifiers for asynchronous requests:
  private static final int LOAD_MASKED_WALLET_REQUEST_CODE = 1000;
  private static final int LOAD_FULL_WALLET_REQUEST_CODE = 1001;

  private GoogleApiClient googleApiClient;

  private SupportWalletFragment walletFragment;
  private PayManualFragment manualFragment;

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    final Wallet.WalletOptions walletOptions =
        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
                                          .setTheme(WalletConstants.THEME_LIGHT)
                                          .build();
    googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                                                       .addOnConnectionFailedListener(this)
                                                       .addApi(Wallet.API, walletOptions)
                                                       .build();

    manualFragment = (PayManualFragment)getSupportFragmentManager().findFragmentById(R.id.manual_fragment);
    walletFragment = (SupportWalletFragment)getSupportFragmentManager().findFragmentById(R.id.wallet_fragment);
    setContentView(R.layout.pay_activity);

    Wallet.Payments.isReadyToPay(googleApiClient).setResultCallback(new ResultCallback<BooleanResult>()
    {
      @Override
      public void onResult(@NonNull BooleanResult booleanResult)
      {
        if (booleanResult.getStatus().isSuccess())
        {
          if (booleanResult.getValue())
          {
            showAndroidPay();
          }
          else
          {
            hideAndroidPay();
            // Hide Android Pay buttons, show a message that Android Pay
            // cannot be used yet, and display a traditional checkout button
          }
        }
        else
        {
          // Error making isReadyToPay call
          Log.e(TAG, "isReadyToPay:" + booleanResult.getStatus());
        }
      }
    });

  }

  private void hideAndroidPay()
  {
    findViewById(R.id.wallet_fragment).setVisibility(View.GONE);
  }

  public void showAndroidPay()
  {
    MaskedWalletRequest maskedWalletRequest;
    maskedWalletRequest = MaskedWalletRequest.newBuilder()
                                             // Request credit card tokenization with Stripe by specifying tokenization parameters:
                                             .setPaymentMethodTokenizationParameters(
                                                 PaymentMethodTokenizationParameters.newBuilder()
                                                                                    .setPaymentMethodTokenizationType(
                                                                                        PaymentMethodTokenizationType.PAYMENT_GATEWAY)
                                                                                    .addParameter("gateway", "stripe")
                                                                                    .addParameter("stripe:publishableKey",
                                                                                                  PUBLISHABLE_KEY)
                                                                                    .addParameter("stripe:version",
                                                                                                  com.stripe.Stripe.VERSION)
                                                                                    .build()).setShippingAddressRequired(false)
                                             // Price set as a decimal:
                                             .setEstimatedTotalPrice("20.00").setCurrencyCode("GBP").build();

    // Set the parameters:
    WalletFragmentInitParams initParams = WalletFragmentInitParams.newBuilder()
                                                                  .setMaskedWalletRequest(maskedWalletRequest)
                                                                  .setMaskedWalletRequestCode(LOAD_MASKED_WALLET_REQUEST_CODE)
                                                                  .build();

    // Initialize the fragment:
    walletFragment.initialize(initParams);
  }

  public void onStart()
  {
    super.onStart();
    googleApiClient.connect();
  }

  public void onStop()
  {
    super.onStop();
    googleApiClient.disconnect();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == LOAD_MASKED_WALLET_REQUEST_CODE)
    { // Unique, identifying constant
      if (resultCode == Activity.RESULT_OK)
      {
        MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                                                               .setCart(Cart.newBuilder()
                                                                            .setCurrencyCode("GBP")
                                                                            .setTotalPrice("20.00")
                                                                            .addLineItem(
                                                                                LineItem.newBuilder() // Identify item being purchased
                                                                                        .setCurrencyCode("GBP")
                                                                                        .setQuantity("1")
                                                                                        .setDescription("Pickup Sports")
                                                                                        .setTotalPrice("20.00")
                                                                                        .setUnitPrice("20.00")
                                                                                        .build())
                                                                            .build())
                                                               .setGoogleTransactionId(maskedWallet.getGoogleTransactionId())
                                                               .build();
        Wallet.Payments.loadFullWallet(googleApiClient, fullWalletRequest, LOAD_FULL_WALLET_REQUEST_CODE);
      }
    }
    else if (requestCode == LOAD_FULL_WALLET_REQUEST_CODE)
    { // Unique, identifying constant
      if (resultCode == Activity.RESULT_OK)
      {
        FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
        String tokenJSON = fullWallet.getPaymentMethodToken().getToken();

        if (Objects.equal(tokenJSON, "TEST_GATEWAY_TOKEN"))
        {
          Log.e(TAG, "Received test token");
        }
        else
        {
          Log.e(TAG, "Received pay token");
          com.stripe.model.Token token = com.stripe.model.Token.GSON.fromJson(tokenJSON, com.stripe.model.Token.class);
        }
      }
      else
      {
        super.onActivityResult(requestCode, resultCode, data);
      }
    }
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult){}

  @Override
  public void onConnected(Bundle bundle){}

  @Override
  public void onConnectionSuspended(int i){}
}
