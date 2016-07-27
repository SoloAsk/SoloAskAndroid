package com.soloask.android.data.bmob;

import android.util.Log;

import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Lebron on 2016/7/12.
 */
public class MineManager {
    private OnGetQuestionListener listener;
    private User mUser;

    public MineManager(User user) {
        mUser = user;
    }

    public MineManager() {
    }

    public void setOnGetQuestionListener(OnGetQuestionListener listener) {
        this.listener = listener;
    }

    public void getQuestions(int skip, int from) {
        BmobQuery<Question> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(skip);
        query.order("-createdAt");
        switch (from) {
            case Constant.KEY_FROM_MY_QUESTION:
                query.include("answerUser,askUser");
                query.addWhereEqualTo("askUser", mUser);
                break;
            case Constant.KEY_FROM_MY_ANSWER:
                query.include("askUser,answerUser");
                query.addWhereEqualTo("answerUser", mUser);
                break;
            case Constant.KEY_FROM_MY_LISTEN:
                query.include("answerUser");
                query.addWhereContains("heardUser", mUser.getObjectId());
                break;
        }
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null) {
                    listener.onSuccess(list);
                } else {
                    listener.onFailed();
                }
            }
        });

    }

    public void dealTimeOut(final Question question, final int status) {
        question.getAskUser().increment("earning", question.getQuesPrice());
        question.getAskUser().increment("income", question.getQuesPrice());
        question.getAskUser().update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("MineManager", " money back successfully");
                    question.setState(status);
                    question.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("MineManager", " update status successfully");
                            }
                        }
                    });
                }
            }
        });
    }

    public interface OnGetQuestionListener {
        void onSuccess(List<Question> list);

        void onFailed();
    }
}
