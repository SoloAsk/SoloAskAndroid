package com.soloask.android.data.bmob;

import android.util.Log;

import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Lebron on 2016/7/12.
 */
public class QuestionDetailManager {
    private OnQuestionDetailListener listener;
    private OnCheckUserHeardListener mUserHeardListener;

    public void setOnQuestionDetailListener(OnQuestionDetailListener listener) {
        this.listener = listener;
    }

    public void setOnCheckUserHeardListener(OnCheckUserHeardListener listener) {
        mUserHeardListener = listener;
    }

    public void getQuestionDetail(String questionId) {
        BmobQuery<Question> query = new BmobQuery<>();
        query.include("askUser,answerUser");
        query.getObject(questionId, new QueryListener<Question>() {
            @Override
            public void done(Question question, BmobException e) {
                if (e == null) {
                    listener.onSuccess(question);
                } else {
                    listener.onFailed();
                }
            }
        });
    }

    /**
     * 更新偷听人数
     *
     * @param question
     */
    private void updateListenersNum(Question question) {
        question.increment("listenerNum");
        question.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("Lebron", "listener ++");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置偷听用户
     *
     * @param question
     * @param user
     */
    public void setHeardUser(final Question question, final User user) {
        BmobRelation bmobRelation = new BmobRelation();
        bmobRelation.add(user);
        question.setHeardUser(bmobRelation);
        question.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateUserEarning(question);
                    updateUserHeardNum(user);
                    updateListenersNum(question);
                    Log.i("Lebron", "successfully set heard User");
                }
            }
        });
    }

    /**
     * 更新我听人数
     *
     * @param user
     */
    private void updateUserHeardNum(User user) {
        user.increment("heardQuesNum");
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("Lebron", "I heard ++");
                }
            }
        });
    }

    /**
     * 更新提问者收入
     *
     * @param question
     */
    private void updateUserEarning(Question question) {
        User questioner = question.getAskUser();
        User respondent = question.getAnswerUser();
        questioner.increment("earning", 0.5);
        questioner.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("AnswerManager", "Questioner has earned some money");
                }
            }
        });
        respondent.increment("earning", 0.5);
        respondent.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("AnswerManager", "Respondent has earned some money");
                }
            }
        });
    }

    /**
     * 判断用户是否已经偷听过
     *
     * @param question
     * @param user
     */
    public void checkUserHeard(Question question, final User user) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("heardUser", new BmobPointer(question));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    Log.i("Lebron", " 查询个数 " + list.size());
                    if (list.size() > 0 && list.contains(user)) {
                        mUserHeardListener.onSuccess(true);
                    } else {
                        mUserHeardListener.onSuccess(false);
                    }
                } else {
                    Log.i("Lebron", " failed ");
                    e.printStackTrace();
                    mUserHeardListener.onFailed();
                }
            }
        });
    }

    public interface OnQuestionDetailListener {
        void onSuccess(Question question);

        void onFailed();
    }

    public interface OnCheckUserHeardListener {
        void onSuccess(boolean userHeard);

        void onFailed();
    }
}
