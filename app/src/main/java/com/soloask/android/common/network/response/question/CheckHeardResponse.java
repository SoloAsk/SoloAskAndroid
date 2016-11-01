package com.soloask.android.common.network.response.question;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.common.network.response.BaseResponse;

/**
 * Created by lebron on 16-10-31.
 */

public class CheckHeardResponse extends BaseResponse {
    @SerializedName("data")
    private String isHeard;

    public boolean isHeard() {
        return isHeard.equals("heard") ? true : false;
    }
}
