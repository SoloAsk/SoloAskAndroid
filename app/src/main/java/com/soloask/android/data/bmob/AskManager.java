package com.soloask.android.data.bmob;

import android.util.Log;

import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.RelativeDateFormat;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Lebron on 2016/7/11.
 */
public class AskManager {
    private OnRespondentQuestionListener mListener;
    private OnAskQuestionListener onAskQuestionListener;

    public void setOnRespondentQuestionListener(OnRespondentQuestionListener listener) {
        mListener = listener;
    }

    public void setOnAskQuestionListener(OnAskQuestionListener listener) {
        onAskQuestionListener = listener;
    }

    public void askQuestion(User respondent, final User questioner, String content, boolean isPub, double price) {
        Question question = new Question();
        question.setQuesContent(content);
        question.setPub(isPub);
        question.setQuesPrice(price);
        question.setAnswerUser(respondent);
        question.setAskUser(questioner);
        question.setListenerNum(0);
        question.setAskTime(RelativeDateFormat.getCurrentTime());
        question.setState(Constant.STATUS_UNANSWERED);
        question.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    updateUserAskNum(questioner);
                    onAskQuestionListener.onSuccess(s);
                } else {
                    onAskQuestionListener.onFailed();
                }
            }
        });
    }

    private void updateUserAskNum(User user) {
        user.increment("askQuesNum");
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("Lebron", "I asked ++");
                }
            }
        });
    }

    public void getHistoryQuestions(int skip, User user) {
        BmobQuery<Question> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(skip);
        query.addWhereExists("quesVoiceURL");
        query.addWhereEqualTo("answerUser", user);
        query.order("-createdAt");
        query.include("askUser");
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null) {
                    mListener.onSuccess(list);
                } else {
                    mListener.onFailed();
                }
            }
        });
    }

    public interface OnRespondentQuestionListener {
        void onSuccess(List<Question> list);

        void onFailed();
    }

    public interface OnAskQuestionListener {
        void onSuccess(String objectId);

        void onFailed();
    }
}
