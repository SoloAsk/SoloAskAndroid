package com.soloask.android.common.network.response.main;

import com.google.gson.annotations.SerializedName;
import com.soloask.android.account.model.UserModel;
import com.soloask.android.common.network.response.BaseResponse;

import java.util.List;

/**
 * Created by lebron on 16-10-28.
 */

public class UserListResponse extends BaseResponse {
    @SerializedName("data")
    private List<UserModel> userModelList;


    public List<UserModel> getUserList() {
        return userModelList;
    }
}
