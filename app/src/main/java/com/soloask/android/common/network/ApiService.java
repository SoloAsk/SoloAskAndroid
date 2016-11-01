package com.soloask.android.common.network;

import com.soloask.android.common.network.request.account.LoginRequest;
import com.soloask.android.common.network.request.account.MineQuesRequest;
import com.soloask.android.common.network.request.account.UpdateUserRequest;
import com.soloask.android.common.network.request.account.UserRequest;
import com.soloask.android.common.network.request.account.WithDrawRequest;
import com.soloask.android.common.network.request.main.DiscoverRequest;
import com.soloask.android.common.network.request.main.HotRequest;
import com.soloask.android.common.network.request.question.AnswerRequest;
import com.soloask.android.common.network.request.question.AskRequest;
import com.soloask.android.common.network.request.question.CheckHeardRequest;
import com.soloask.android.common.network.request.question.QuesDetailRequest;
import com.soloask.android.common.network.request.search.SearchRequest;
import com.soloask.android.common.network.response.WithDrawResponse;
import com.soloask.android.common.network.response.account.UserResponse;
import com.soloask.android.common.network.response.main.UserListResponse;
import com.soloask.android.common.network.response.main.QuesListResponse;
import com.soloask.android.common.network.response.question.CheckHeardResponse;
import com.soloask.android.common.network.response.question.QuestionResponse;
import com.soloask.android.common.network.response.question.SetHeardResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lebron on 16-10-27.
 */

public interface ApiService {
    @POST("api/user/login")
    Observable<UserResponse> userLogin(@Body LoginRequest request);

    @POST("api/user/info")
    Observable<UserResponse> getUserInfo(@Body UserRequest request);

    @POST("api/user/update")
    Observable<UserResponse> updateUser(@Body UpdateUserRequest request);

    @POST("api/user/list")
    Observable<UserListResponse> getUserList(@Body DiscoverRequest request);

    @POST("api/question/hot")
    Observable<QuesListResponse> getHotQues(@Body HotRequest request);

    @POST("api/question/related")
    Observable<QuesListResponse> getRelatedQues(@Body MineQuesRequest request);

    @POST("api/heard/list")
    Observable<QuesListResponse> getListenedQues(@Body MineQuesRequest request);

    @POST("api/withdraw/create")
    Observable<WithDrawResponse> createWithDraw(@Body WithDrawRequest request);

    @POST("api/question/detail")
    Observable<QuestionResponse> getQuesDetail(@Body QuesDetailRequest request);

    @POST("api/heard/check")
    Observable<CheckHeardResponse> checkQuesHeard(@Body CheckHeardRequest request);

    @POST("api/heard/add")
    Observable<SetHeardResponse> setHeardUser(@Body CheckHeardRequest request);

    @POST("api/question/create")
    Observable<QuestionResponse> createQuestion(@Body AskRequest request);

    @POST("api/question/answer")
    Observable<QuestionResponse> answerQuestion(@Body AnswerRequest request);

    @POST("api/question/similar")
    Observable<QuesListResponse> searchQuestions(@Body SearchRequest request);

    @POST("api/user/similar")
    Observable<UserListResponse> searchUsers(@Body SearchRequest request);
}
