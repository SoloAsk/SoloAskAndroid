package com.soloask.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.soloask.android.R;

/**
 * Created by Lebron on 2016/6/24.
 */
public class Constant {
    public static final String BASE64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjrE9YpIyhj3xbKuawz/6nqyKhOrAjSr/rXfiFIFb2nWtwpCdoIpYM9HGEjEDSE6iwwNZbHxRlezbjpdMxoxkkkQE8y1T2Lz2xsyPp+OJ7xhNaf28Mcj9gZ7J1Lr1zL6jKTw+RDk8HgMM0dmZXzAwCGq4iPH+KaZc94kr3xcTfg6FdV3N1cxq6UcWezusyUoqwhnE52KEgwPzRduwsKjr4xcZGro4ClQ9XocIuTNiVMoDXOaTCrAynhYp5bzlEKcnmByDntqF8rGxShIIGJ/NCySBehnEWhA0wstXAIKJt3fCunhSKTLuR/lgUXxvJMpBnLeaoTpky8/Ua7PHj3qZKwIDAQAB";
    public static final String QQ_APP_ID = "1105502369";
    public static final String KEY_FROM_MINE = "key_from_mine";
    public static final String KEY_FROM_SEARCH = "key_from_search";
    public static final String KEY_SEARCH_CONTENT = "key_search_content";
    public static final String KEY_LOGINED_OBJECT_ID = "key_is_logined";
    public static final String KEY_LOGINED_ICON_URL = "key_logined_icon_url";
    public static final String KEY_REMEMBER_PAYPAL = "key_remember_paypal";
    public static final String KEY_PAYPAL_ACCOUNT = "key_paypal_account";
    public static final String KEY_QUESTION_ID = "key_question_id";
    public static final String FILE_NAME_VOICE = "answer_temp.aac";
    public static final String OVERHEAR_PRICE_ID = "payment_1";
    public static final int[] ARRAY_STATUS = new int[]{R.string.status_unanswered, R.string.status_answered, R.string.status_refunded, R.string.status_timeout};

    public static final int CODE_RESULT_LOGIN = 100;
    public static final int CODE_RESULT_EDIT = 200;
    public static final int CODE_REQUEST = 0;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 10;

    public static final int KEY_FROM_MY_QUESTION = 10;
    public static final int KEY_FROM_MY_ANSWER = 20;
    public static final int KEY_FROM_MY_LISTEN = 30;
    public static final int STATUS_UNANSWERED = 0;//待回答
    public static final int STATUS_ANSWERED = 1;//已回答
    public static final int STATUS_REFUND = 2;//已退款
    public static final int STATUS_TIMEOUT = 3;//已过期

    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "1.0.0";
    }
}
