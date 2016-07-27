package com.soloask.android.data.bmob;


import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Lebron on 2016/7/18.
 */
public class SearchManager {
    private OnSearchUserListener mUserListener;
    private OnSearchQuestionListener mQuestionsListener;

    public void setOnSearchUserListener(OnSearchUserListener listener) {
        mUserListener = listener;
    }

    public void setOnSearchQuestionListener(OnSearchQuestionListener listener) {
        mQuestionsListener = listener;
    }

    public void getSearchUsers(String text, int limit, int skipNum) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereContains("userName", text);
        query.setLimit(limit);
        query.setSkip(skipNum);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    mUserListener.onSuccess(list);
                } else {
                    mUserListener.onFailed();
                }
            }
        });
    }

    public void getSearchQuestions(String text, int limit, int skipNum) {
        BmobQuery<Question> query = new BmobQuery<>();
        query.addWhereContains("quesContent", text);
        query.include("answerUser");
        query.setLimit(limit);
        query.setSkip(skipNum);
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null) {
                    mQuestionsListener.onSuccess(list);
                } else {
                    mQuestionsListener.onFailed();
                }
            }
        });
    }

    public interface OnSearchUserListener {
        void onSuccess(List users);

        void onFailed();
    }

    public interface OnSearchQuestionListener {
        void onSuccess(List questions);

        void onFailed();
    }
}
