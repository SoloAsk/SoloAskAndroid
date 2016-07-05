package com.soloask.android.util;

import android.content.Context;
import android.widget.Toast;

import com.soloask.android.R;
import com.soloask.android.util.billing.IabHelper;
import com.soloask.android.util.billing.IabResult;

/**
 * Created by Lebron on 2016/6/29.
 */
public class GooglePayManager {
    private static GooglePayManager mGooglePayManager;
    private IabHelper mHelper;

    public static GooglePayManager getInstance(Context context) {
        if (mGooglePayManager == null) {
            mGooglePayManager = new GooglePayManager(context);
        }
        return mGooglePayManager;
    }

    public GooglePayManager(final Context context) {
        mHelper = new IabHelper(context, context.getResources().getString(R.string.base64_encoded_public_key));
        mHelper.enableDebugLogging(false);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

}
