package com.soloask.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soloask.android.R;
import com.soloask.android.activity.QuestionDetailActivity;
import com.soloask.android.util.Constant;
import com.soloask.android.util.billing.IabHelper;
import com.soloask.android.util.billing.IabResult;
import com.soloask.android.util.billing.Purchase;

import java.util.List;


/**
 * Created by Lebron on 2016/6/21.
 */
public class HistoryQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Activity mContext;
    private List mDatas;
    private IabHelper mHelper;

    public HistoryQuestionAdapter(Activity context, List list) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = list;
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).questionView.setText(mDatas.get(position).toString());
            ((ItemViewHolder) holder).timeView.setText(mContext.getResources().getQuantityString(R.plurals.dealed_time_hour, 1, 2));
            ((ItemViewHolder) holder).listenersView.setText(String.format(mContext.getResources().getString(R.string.format_listerers), 123));
            ((ItemViewHolder) holder).voiceView.setText(String.format(mContext.getResources().getString(R.string.format_price), 2.99));
        } else if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).mSummaryView.setText(mContext.getResources().getQuantityString(R.plurals.answer_and_earn, 1, 43));
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
        } else if (position + 1 == getItemCount()) {
            return Constant.TYPE_FOOTER;
        } else {
            return Constant.TYPE_ITEM;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout historyLayout;
        TextView questionView;
        TextView priceView;
        TextView timeView;
        TextView listenersView;
        TextView voiceView;

        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            questionView = (TextView) itemView.findViewById(R.id.tv_questioner_question);
            //priceView = (TextView) itemView.findViewById(R.id.tv_questioner_question);
            timeView = (TextView) itemView.findViewById(R.id.tv_answered_time);
            voiceView = (TextView) itemView.findViewById(R.id.tv_voice_price);
            listenersView = (TextView) itemView.findViewById(R.id.tv_listeners_info);
            historyLayout = (RelativeLayout) itemView.findViewById(R.id.rl_history_question);
            historyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QuestionDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView mFinishView;
        TextView mEditCountView;
        EditText mQuestionView;
        TextView mSummaryView;

        public HeadViewHolder(View itemView, Context context) {
            super(itemView);
            mFinishView = (TextView) itemView.findViewById(R.id.btn_submit_question);
            mEditCountView = (TextView) itemView.findViewById(R.id.tv_edit_count);
            mSummaryView = (TextView) itemView.findViewById(R.id.tv_user_history);
            mQuestionView = (EditText) itemView.findViewById(R.id.edit_ask_question);
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
            mFinishView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(mQuestionView.getText())) {
                        mQuestionView.setError(mContext.getString(R.string.notice_cannot_null));
                    } else {
                        doPurchase();
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

    private void doPurchase() {
        if (mHelper != null && !mHelper.isAsyncInProgress()) {
            mHelper.launchPurchaseFlow(mContext, "payment_for_listen", 10002, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isFailure()) {
                        Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if (info.getSku().equals("payment_for_listen")) {
                            doConsume(info);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(mContext, R.string.google_pay_is_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void doConsume(Purchase purchase) {
        mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isFailure()) {
                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(mContext, QuestionDetailActivity.class);
                intent.putExtra(Constant.KEY_FROM_MYASK, true);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "Asked successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setIABHelper(IabHelper iabHelper) {
        mHelper = iabHelper;
    }
}
