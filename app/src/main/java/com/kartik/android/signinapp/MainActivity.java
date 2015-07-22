package com.kartik.android.signinapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.Environment;
import com.citrus.sdk.classes.AccessToken;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class MainActivity extends Activity {

    // tag for logs
    private final String TAG = "MainActivity";

    // important constants
    private static final String ACCOUNT_TYPE = "com.citrus.oauth_citruspay";
    private static final String AUTH_TOKEN_TYPE = "com.citrus.oauth_citruspay";

    // members representing layout elements
    private static Button mAddAccountButton;
    private static Button mGetAuthtokenButton;

    // AccountManager member
    private static AccountManager mAccountManager;

    // member Bundle that will hold client's credentials
    private Bundle mAccountOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-------------------------------------
        //     setting member values
        //-------------------------------------

        mAccountOptions = new Bundle();
        mAccountOptions.putString("KEY_SIGNUP_ID", "test-signup");
        mAccountOptions.putString("KEY_SIGNUP_SECRET", "c78ec84e389814a05d3ae46546d16d2e");
        mAccountOptions.putString("KEY_SIGNIN_ID", "test-signin");
        mAccountOptions.putString("KEY_SIGNIN_SECRET", "52f7e15efd4208cf5345dd554443fd99");
        mAccountOptions.putString("KEY_VANITY", "testing");
        mAccountOptions.putString("KEY_ACCOUNT_TYPE", "com.citrus.oauth_citruspay");

        mAccountManager = AccountManager.get(this);

        //-------------------------------------
        //  setting up button actions
        //-------------------------------------

        mAddAccountButton = (Button)findViewById(R.id.add_account_button);
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });

        mGetAuthtokenButton = (Button)findViewById(R.id.get_authtoken_button);
        mGetAuthtokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAuthToken();
            }
        });
    }

    // adds the given user's Citrus account to the list of accounts on the device,
    // after verifying the user's credentials by attempting to sign in
    public void addAccount() {
        //---------------------------------------------------------------------------------
        // calling the required method of the AccountManager object, which in turn uses the
        // Authenticator's addAccount method
        //---------------------------------------------------------------------------------

        final AccountManagerFuture<Bundle> addAccount_result = mAccountManager.addAccount(
                ACCOUNT_TYPE, AUTH_TOKEN_TYPE, null, mAccountOptions, this, null, null);

        //----------------------------------------------------------------------
        // handling the output of the AccountManager object's addAccount method
        //----------------------------------------------------------------------

        // the actions below are carried out on a new thread because they might
        // make the main thread stop
        new Thread(new Runnable() {
            public void run() {
                try {
                    // converting the result into a bundle
                    final Bundle addAccount_bundle = addAccount_result.getResult();

                    // displaying a toast to indicate the outcome of the procedure
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            String addAccountMessage;
                            if (addAccount_bundle.getInt("KEY_CODE") == 0) {
                                addAccountMessage = "Signed in successfully, account added";
                            } else if (addAccount_bundle.getInt("KEY_CODE") == 1) {
                                addAccountMessage = "Could not sign in";
                            } else {
                                addAccountMessage = "Signed in successfully, account not added";
                            }
                            displayToast(addAccountMessage);
                        }
                    });
                } catch (Exception e) {
                    err("Could not convert addAccount_result into Bundle because "
                            + e.getMessage());
                }
            }
        }).start();
    }

    // retrieves an auth token for the Citrus account stored on the device
    public void getAuthToken() {

        //-------------------------------------------------------------------------
        //  getting the user's Citrus account through the AccountManager object
        //-------------------------------------------------------------------------

        Account citrusAccount_juvenile = null;
        Account[] citrusAccount_array = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
        if (citrusAccount_array.length > 0) {
            citrusAccount_juvenile = citrusAccount_array[0];
        }
        final Account citrusAccount = citrusAccount_juvenile;

        //---------------------------------------------------------------------------------
        // calling the required method of the AccountManager object, which in turn uses the
        // Authenticator's getAuthToken method
        //---------------------------------------------------------------------------------

        final AccountManagerFuture<Bundle> getAuthToken_result = mAccountManager.getAuthToken(citrusAccount,
                AUTH_TOKEN_TYPE, mAccountOptions, this, null, null);

        //------------------------------------------------------------------------
        // handling the output of the AccountManager object's getAuthToken method
        //------------------------------------------------------------------------

        // the actions below are carried out on a new thread because they might
        // make the main thread stop
        new Thread(new Runnable() {
            public void run() {
                try {
                    // trying to convert getAuthToken_result into a Bundle object
                    final Bundle getAuthToken_bundle = getAuthToken_result.getResult();

                    // displaying a toast to indicate the outcome of the procedure
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            String getAuthTokenMessage;
                            if (getAuthToken_bundle.getInt("KEY_CODE") == 0) {
                                getAuthTokenMessage = "Received an access token " +
                                        getAuthToken_bundle.getString("KEY_AUTHTOKEN");
                            } else if (getAuthToken_bundle.getInt("KEY_CODE") == 1) {
                                getAuthTokenMessage = "Could not sign in";
                            } else {
                                getAuthTokenMessage = "Signed in, could not get an auth token";
                            }
                            displayToast(getAuthTokenMessage);
                        }
                    });
                } catch (Exception e) {
                    err("Could not convert getAuthToken_result into Bundle because "
                            + e.getMessage());
                }
            }
        }).start();
    }

    // prints debug messages to the console
    private void log(String message) {
        Log.d(TAG, message);
    }

    // prints error messages to the console
    private void err(String message) {
        Log.e(TAG, message);
    }

    // displays toasts
    private void displayToast(String message) {
        Toast toast = Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
