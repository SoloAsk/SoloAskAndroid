package com.soloask.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.activity.AnswerActivity;
import com.soloask.android.util.Constant;

import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class MyAnswerAdapter extends BaseAdapter {

    public MyAnswerAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.layout_footer, parent, false));
        } else {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_my_question_view, parent, false), mContext);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).nameView.setText(mDatas.get(position).toString());
            ((ItemViewHolder) holder).timeView.setText(mContext.getResources().getQuantityString(R.plurals.dealed_time_hour, 1, 2));
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView;
        TextView priceView;
        TextView statusView;
        TextView questionView;
        TextView timeView;
        TextView listenersView;
        LinearLayout mContainer;


        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_questioner);
            nameView = (TextView) itemView.findViewById(R.id.tv_questioner_name);
            priceView = (TextView) itemView.findViewById(R.id.tv_cost);
            statusView = (TextView) itemView.findViewById(R.id.tv_status);
            questionView = (TextView) itemView.findViewById(R.id.tv_question);
            timeView = (TextView) itemView.findViewById(R.id.tv_answered_time);
            listenersView = (TextView) itemView.findViewById(R.id.tv_listeners_info);
            mContainer = (LinearLayout) itemView.findViewById(R.id.ll_my_question_item);
            statusView.setVisibility(View.VISIBLE);
            listenersView.setVisibility(View.GONE);

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnswerActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
