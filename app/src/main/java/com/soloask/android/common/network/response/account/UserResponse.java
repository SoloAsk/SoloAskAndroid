package com.soloask.android.common.network.response.account;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.network.response.BaseResponse;

/**
 * Created by lebron on 16-10-28.
 */

public class UserResponse extends BaseResponse {
    @SerializedName("data")
    private UserModel mUserModel;


    public UserModel getUserModel(){
        return mUserModel;
    }
}
