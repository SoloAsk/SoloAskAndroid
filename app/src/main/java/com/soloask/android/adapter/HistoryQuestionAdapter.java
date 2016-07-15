package com.soloask.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soloask.android.R;
import com.soloask.android.activity.QuestionDetailActivity;
import com.soloask.android.data.bmob.AskManager;
import com.soloask.android.data.model.Question;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.RelativeDateFormat;
import com.soloask.android.util.billing.IabHelper;
import com.soloask.android.util.billing.IabResult;
import com.soloask.android.util.billing.Inventory;
import com.soloask.android.util.billing.Purchase;
import com.soloask.android.view.BoundImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lebron on 2016/6/21.
 */
public class HistoryQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private List<Question> mDatas;
    private IabHelper mHelper;
    private User mRespondent, mQuestioner;
    private String mContent;
    private boolean isPub = true;
    private double mPrice;
    private HeadViewHolder headerViewHolder;
    private String mPaymentId;

    public HistoryQuestionAdapter(Activity context, User respondent, List list) {
        this.mContext = context;
        mRespondent = respondent;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = list;
        mPaymentId = "payment_" + mRespondent.getUserPrice();
    }

    public void setQuestioner(User questioner) {
        mQuestioner = questioner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_HEADER) {
            return new HeadViewHolder(mLayoutInflater.inflate(R.layout.headview_user_profile, parent, false), mContext);
        } else if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_user_voice_history, parent, false), mContext);
        }
    }

    public void updateHeaderView(User user) {
        mRespondent = user;
        mPaymentId = "payment_" + mRespondent.getUserPrice();
        notifyItemChanged(0);
    }

    private void setRespondentInfo() {
        String summary = String.format(mContext.getResources().getString(R.string.format_answered), mRespondent.getAnswerQuesNum())
                + " , " + String.format(mContext.getResources().getString(R.string.format_earned), mRespondent.getUserIncome());
        headerViewHolder.mSummaryView.setText(summary);
        headerViewHolder.mNameView.setText(mRespondent.getUserName());
        headerViewHolder.mTitleView.setText(mRespondent.getUserTitle());
        headerViewHolder.mIntroduceView.setText(mRespondent.getUserIntroduce());
        headerViewHolder.mPriceView.setText(String.format(mContext.getString(R.string.format_dollar), mRespondent.getUserPrice()));
        mPrice = mRespondent.getUserPrice();
        Glide.with(mContext)
                .load(mRespondent.getUserIcon())
                //.placeholder(R.drawable.ic_me_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(headerViewHolder.mIconView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final Question question = mDatas.get(position);
            Glide.with(mContext)
                    .load(question.getAskUser().getUserIcon())
                    //.placeholder(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemViewHolder) holder).questionerIcon);
            Glide.with(mContext)
                    .load(mRespondent.getUserIcon())
                    //.placeholder(R.drawable.ic_me_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemViewHolder) holder).respondentIcon);
            ((ItemViewHolder) holder).questionView.setText(question.getQuesContent());
            ((ItemViewHolder) holder).timeView.setText(RelativeDateFormat.format(question.getAskTime(), mContext));
            ((ItemViewHolder) holder).timeLengthView.setText(String.format(mContext.getString(R.string.format_second), question.getVoiceTime()));
            ((ItemViewHolder) holder).listenersView.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).listenersView.setText(String.format(mContext.getString(R.string.format_listerers), question.getListenerNum()));
            ((ItemViewHolder) holder).priceView.setText(String.format(mContext.getString(R.string.format_dollar), question.getQuesPrice()));
            ((ItemViewHolder) holder).listenersView.setText(String.format(mContext.getResources().getString(R.string.format_listerers), question.getListenerNum()));
            //((ItemViewHolder) holder).voiceView.setText(String.format(mContext.getResources().getString(R.string.format_price), 2.99));
            ((ItemViewHolder) holder).historyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                    intent.putExtra(Constant.KEY_QUESTION_ID, question.getObjectId());
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof HeadViewHolder) {
            headerViewHolder = (HeadViewHolder) holder;
            setRespondentInfo();
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constant.TYPE_HEADER;
        } else if (position == getItemCount() && getItemCount() >= 10) {
            return Constant.TYPE_FOOTER;
        } else {
            return Constant.TYPE_ITEM;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout historyLayout;
        BoundImageView questionerIcon;
        BoundImageView respondentIcon;
        TextView questionView;
        TextView priceView;
        TextView timeView;
        TextView timeLengthView;
        TextView listenersView;
        TextView voiceView;

        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            questionerIcon = (BoundImageView) itemView.findViewById(R.id.img_questioner);
            respondentIcon = (BoundImageView) itemView.findViewById(R.id.img_respondent);
            questionView = (TextView) itemView.findViewById(R.id.tv_questioner_question);
            priceView = (TextView) itemView.findViewById(R.id.tv_question_price);
            timeView = (TextView) itemView.findViewById(R.id.tv_answered_time);
            timeLengthView = (TextView) itemView.findViewById(R.id.tv_time_length);
            voiceView = (TextView) itemView.findViewById(R.id.tv_voice_price);
            listenersView = (TextView) itemView.findViewById(R.id.tv_listeners_info);
            historyLayout = (RelativeLayout) itemView.findViewById(R.id.rl_history_question);
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {
        BoundImageView mIconView;
        TextView mNameView, mTitleView, mIntroduceView, mPriceView;
        TextView mFinishView;
        TextView mEditCountView;
        EditText mQuestionView;
        CheckBox mPubBox;
        TextView mSummaryView;

        public HeadViewHolder(View itemView, Context context) {
            super(itemView);
            mIconView = (BoundImageView) itemView.findViewById(R.id.img_user_icon);
            mNameView = (TextView) itemView.findViewById(R.id.tv_user_name);
            mTitleView = (TextView) itemView.findViewById(R.id.tv_user_title);
            mIntroduceView = (TextView) itemView.findViewById(R.id.tv_user_describe);
            mPriceView = (TextView) itemView.findViewById(R.id.tv_question_price);
            mFinishView = (TextView) itemView.findViewById(R.id.btn_submit_question);
            mEditCountView = (TextView) itemView.findViewById(R.id.tv_edit_count);
            mSummaryView = (TextView) itemView.findViewById(R.id.tv_user_history);
            mQuestionView = (EditText) itemView.findViewById(R.id.edit_ask_question);
            mPubBox = (CheckBox) itemView.findViewById(R.id.checkbox_question_nameless);
            mQuestionView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mEditCountView.setText(String.format(mContext.getString(R.string.format_write_question), mQuestionView.getText().length()));
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mPubBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isPub = true;
                    } else {
                        isPub = false;
                    }
                }
            });
            mFinishView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContent = mQuestionView.getText().toString();
                    if (TextUtils.isEmpty(mContent)) {
                        mQuestionView.setError(mContext.getString(R.string.notice_cannot_null));
                    } else if (mQuestioner.getObjectId().equals(mRespondent.getObjectId())) {
                        Toast.makeText(mContext, R.string.notice_ask_yourself, Toast.LENGTH_SHORT).show();
                    } else {
                        doPurchase(mQuestionView);
                    }
                }
            });
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void doPurchase(final View view) {
        if (mHelper != null && !mHelper.isAsyncInProgress()) {
            mHelper.launchPurchaseFlow(mContext, mPaymentId, 10002, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isFailure()) {
                        Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if (info.getSku().equals(mPaymentId)) {
                            doConsume(info, view, false);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(mContext, R.string.google_pay_is_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void doConsume(Purchase purchase, final View view, final boolean fromQuery) {
        mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isFailure()) {
                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!fromQuery) {
                    AskManager askManager = new AskManager();
                    askManager.setOnAskQuestionListener(new AskManager.OnAskQuestionListener() {
                        @Override
                        public void onSuccess(String objectId) {
                            Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                            intent.putExtra(Constant.KEY_QUESTION_ID, objectId);
                            mContext.startActivity(intent);
                            if (view != null) {
                                ((EditText) view).setText("");
                            }
                            Toast.makeText(mContext, R.string.notice_success, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailed() {
                            Toast.makeText(mContext, "Something wrong", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (mRespondent != null && mQuestioner != null) {
                        askManager.askQuestion(mRespondent, mQuestioner, mContent, isPub, mPrice);
                    }
                } else {
                    Log.i("Lebron", "consume successfully");
                }

            }
        });
    }

    private void doQuery() {
        List mSKULists = new ArrayList();
        if (mPaymentId == null) {
            mPaymentId = Constant.OVERHEAR_PRICE_ID;
        }
        mSKULists.add(mPaymentId);
        if (mHelper != null) {
            mHelper.queryInventoryAsync(true, mSKULists, new IabHelper.QueryInventoryFinishedListener() {

                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    boolean hasPurchase = inv.hasPurchase(mPaymentId);
                    if (hasPurchase) {
                        try {
                            doConsume(inv.getPurchase(mPaymentId), null, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Lebron", hasPurchase + " " + inv.getSkuDetails("payment_for_listen").toString());
                    //String priceResult = inv.getSkuDetails("payment_for_listen").getPrice();

                }
            });
        } else {
            Toast.makeText(mContext, R.string.google_pay_is_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    public void setIABHelper(IabHelper iabHelper) {
        mHelper = iabHelper;
        doQuery();
    }
}
